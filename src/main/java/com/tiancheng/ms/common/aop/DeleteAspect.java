package com.tiancheng.ms.common.aop;


import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.ProduceProcessConstant;
import com.tiancheng.ms.constant.ProduceProcessStatusConstant;
import com.tiancheng.ms.dao.mapper.*;
import com.tiancheng.ms.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Aspect
@Slf4j
@Component
@Order(2)
public class DeleteAspect {

    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    @Autowired
    private ProductProcessMapper productProcessMapper;

    @Autowired
    private ProduceMapper produceMapper;

    @Autowired
    private ProduceProductMapper produceProductMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProduceProcessMapper produceProcessMapper;

    @Autowired
    private ProduceProcessConstant produceProcessConstant;

    @Autowired
    private ProductMapper productMapper;


    @Pointcut("@annotation(com.tiancheng.ms.common.aop.DeleteType)")
    public void deleteMethod() {
    }

    @Before("deleteMethod() && @annotation(type)")
    public void beforeDelete(JoinPoint point, DeleteType type) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Map<String,String> pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        switch (type.value()) {
            case MATERIAL:
                {
                    beforeDeleteMaterial(Integer.valueOf(pathVariables.get(type.id())));
                    break;
                }
            case PROCESS:
                {
                    beforeDeleteProcess(Integer.valueOf(pathVariables.get(type.id())));
                    break;
                }
            case PRODUCE:
                {
                    beforeDeleteProduce(Integer.valueOf(pathVariables.get(type.id())));
                    break;
                }
            case PRODUCT:
                {
                    beforeDeleteProduct(Integer.valueOf(pathVariables.get(type.id())));
                    break;
                }
            default:return;
        }
    }

    private void beforeDeleteMaterial(Integer materialId) {
        MaterialEntity entity = materialMapper.selectByPrimaryKey(materialId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.FAIL, "删除内容不存在");
        }
        List<ProductMaterial> productMaterials = productMaterialMapper.selectByMaterialId(materialId);
        if (!CollectionUtils.isEmpty(productMaterials)) {
            throw new BusinessException(ErrorCode.FAIL,"删除的材料【" + entity.getCode() + "】已被引用,无法删除！" );
        }
        return;
    }

    private void beforeDeleteProcess(Integer processId) {
        ProcessEntity entity = processMapper.selectByPrimaryKey(processId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.FAIL, "删除流程不存在！");
        }
        List<ProductProcessEntity> productProcessEntities = productProcessMapper.selectByProcessId(processId);
        if (!CollectionUtils.isEmpty(productProcessEntities)) {
            throw new BusinessException(ErrorCode.FAIL,"删除的流程【" + entity.getName() + "】已被引用,无法删除！");
        }
        return;
    }

    private void beforeDeleteProduce(Integer produceId) {
        ProduceEntity entity = produceMapper.selectByPrimaryKey(produceId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"删除产品不存在！");
        }
        ProduceProcessEntity endProduceProcess = produceProcessMapper.selectEndProduceProcess(produceId,produceProcessConstant.getEndId());
        if (endProduceProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，生产计划没有终止流程！");
        }
        if (endProduceProcess.getStatus() == ProduceProcessStatusConstant.INIT_STATUS) {
            throw new BusinessException(ErrorCode.FAIL,"删除的生产计划【" + entity.getCode() + "】尚未结束");
        }
        ProduceProcessEntity startProduceProcess = produceProcessMapper.selectStartProduceProcess(produceId,produceProcessConstant.getStartId());
        if (startProduceProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，生产计划没有开始流程！");
        }
        if (startProduceProcess.getStatus() != ProduceProcessStatusConstant.ARRIVE_STATUS) {
            throw new BusinessException(ErrorCode.FAIL,"删除的生产计划【" + entity.getCode() + "】已经开始");
        }
        return;
    }

    private void beforeDeleteProduct(Integer productId) {
        ProductEntity entity = productMapper.selectByPrimaryKey(productId);
        if (entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"删除的产品不存在！");
        }
        List<ProduceProductEntity> produceProductEntities = produceProductMapper.getUnoverProducesByProductId(productId);
        if (!CollectionUtils.isEmpty(produceProductEntities)) {
            throw new BusinessException(ErrorCode.FAIL,"删除的产品【" + entity.getCode() + "】已经被引用！");
        }
        return;
    }
}
