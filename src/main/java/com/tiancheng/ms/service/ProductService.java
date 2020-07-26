package com.tiancheng.ms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.dto.PageRequestWrapper;
import com.tiancheng.ms.common.dto.SelectOption;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.ExampleConstant;
import com.tiancheng.ms.dao.mapper.*;
import com.tiancheng.ms.dto.ProductDetailDTO;
import com.tiancheng.ms.dto.param.ProductDetailParam;
import com.tiancheng.ms.dto.param.ProductParam;
import com.tiancheng.ms.dto.param.ProductQueryParam;
import com.tiancheng.ms.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductProcessMapper productProcessMapper;
    @Autowired
    private ProductMaterialMapper productMaterialMapper;
    @Autowired
    private RulesMapper rulesMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProcessMapper processMapper;

    @Transactional(rollbackFor = Exception.class)
    public void addProduct(ProductDetailParam detailParam) {
        ProductParam productParam = detailParam.getProductParam();
        Example example = new Example(ProductEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo(ExampleConstant.PRODUCT_CODE,productParam.getCode());
        ProductEntity productEntity = productMapper.selectOneByExample(example);
        if (productEntity != null) {
            throw new BusinessException(ErrorCode.FAIL, "产品编码重复");
        }
        productEntity = new ProductEntity();
        BeanUtils.copyProperties(productParam, productEntity);
        productEntity.setCreateBy(ContextHolder.getUser().getRealName());
        productEntity.setCreateTime(new Date());
        productMapper.insertSelective(productEntity);

        if (!CollectionUtils.isEmpty(detailParam.getProcessParams())) {
            ProductEntity finalProductEntity = productEntity;
            detailParam.getProcessParams().stream().forEach(entity -> {
                entity.setProductId(finalProductEntity.getId());
            });
            productProcessMapper.insertList(detailParam.getProcessParams());
        }

        if (!CollectionUtils.isEmpty(detailParam.getMaterialParams())) {
            ProductEntity finalProductEntity1 = productEntity;
            detailParam.getMaterialParams().stream().forEach(entity -> {
                entity.setProductId(finalProductEntity1.getId());
            });
            productMaterialMapper.insertList(detailParam.getMaterialParams());
        }

        if (!CollectionUtils.isEmpty(detailParam.getRuleParams())) {
            ProductEntity finalProductEntity2 = productEntity;
            detailParam.getRuleParams().stream().forEach(entity -> {
                entity.setProductId(finalProductEntity2.getId());
            });
            rulesMapper.insertList(detailParam.getRuleParams());
        }
    }

    public Page<ProductEntity> pageQryProduct(PageRequestWrapper<ProductQueryParam> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        if (pageRequest.getQueryParam().getName() != null) {
            pageRequest.getQueryParam().setName(pageRequest.getQueryParam().getName().trim());
        }
        if (pageRequest.getQueryParam().getCode() != null) {
            pageRequest.getQueryParam().setCode(pageRequest.getQueryParam().getCode().trim());
        }
        List<ProductEntity> entities = productMapper.queryByParam(pageRequest.getQueryParam());
        PageInfo pageInfo = new PageInfo(entities);
        return new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), entities);
    }

    public ProductDetailDTO detail(@NotNull Integer productId) {
        ProductEntity productEntity = productMapper.selectByPrimaryKey(productId);
        if (productEntity == null) {
            throw new BusinessException(ErrorCode.FAIL, "产品不存在，请重试！");
        }
        List<MaterialEntity> materialEntities = materialMapper.selectByProductId(productId);
        List<ProcessEntity> processEntities = processMapper.selectByProductId(productId);
        List<RulesEntity> rulesEntities = rulesMapper.selectByProductId(productId);
        return new ProductDetailDTO(productEntity, materialEntities, processEntities, rulesEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductDetailParam detailParam) {
        ProductEntity productEntity = productMapper.selectOneByCode(detailParam.getProductParam().getCode());
        if (productEntity != null && productEntity.getId() != detailParam.getProductParam().getId()) {
            throw new BusinessException(ErrorCode.FAIL, "产品编码重复");
        }
        productEntity = productMapper.selectByPrimaryKey(detailParam.getProductParam().getId());
        if (productEntity == null) {
            throw new BusinessException(ErrorCode.FAIL, "产品不存在，请重试！");
        }
        BeanUtils.copyProperties(detailParam.getProductParam(), productEntity);
        productMapper.updateByPrimaryKeySelective(productEntity);

        List<Integer> existProcIds = productProcessMapper.selectProcessByProductId(detailParam.getProductParam().getId()).stream()
                .map(entity -> entity.getProcessId())
                .collect(Collectors.toList());
        List<Integer> addProcIds = detailParam.getProcessParams().stream()
                .map(entity -> entity.getProcessId())
                .collect(Collectors.toList());
        List<Integer> removeProcIds = new ArrayList<>(existProcIds);
        removeProcIds.removeAll(addProcIds);
        addProcIds.removeAll(existProcIds);
        if (!CollectionUtils.isEmpty(removeProcIds)) {
            productProcessMapper.deleteByIds(StringUtils.arrayToCommaDelimitedString(removeProcIds.toArray()));
        }
        if (!CollectionUtils.isEmpty(addProcIds)) {
            List<ProductProcessEntity> prodProcRelationEntities = addProcIds.stream().map(id -> {
                return new ProductProcessEntity(detailParam.getProductParam().getId(), id);
            }).collect(Collectors.toList());
            productProcessMapper.insertList(prodProcRelationEntities);
        }

        productMaterialMapper.deleteByProductId(detailParam.getProductParam().getId());
        if (!CollectionUtils.isEmpty(detailParam.getMaterialParams())) {
            detailParam.getMaterialParams().stream().forEach(entity -> {
                entity.setProductId(detailParam.getProductParam().getId());
            });
            productMaterialMapper.insertList(detailParam.getMaterialParams());
        }

        rulesMapper.deleteByProductId(detailParam.getProductParam().getId());
        if (!CollectionUtils.isEmpty(detailParam.getRuleParams())) {
            detailParam.getRuleParams().stream().forEach(entity -> {
                entity.setProductId(detailParam.getProductParam().getId());
            });
            rulesMapper.insertList(detailParam.getRuleParams());
        }
    }

    public void deleteProduct(@NotNull Integer productId) {
        if (productMapper.selectByPrimaryKey(productId) == null) {
            throw new BusinessException(ErrorCode.FAIL, "删除产品信息不存在");
        }
        productMapper.deleteByPrimaryKey(productId);
        // TODO: 2020/5/4 删除relation表内容
    }

    public List<SelectOption> optionList() {
        return productMapper.selectAll().stream()
                .map(productEntity -> new SelectOption(productEntity.getCode(),productEntity.getName()))
                .collect(Collectors.toList());
    }
}
