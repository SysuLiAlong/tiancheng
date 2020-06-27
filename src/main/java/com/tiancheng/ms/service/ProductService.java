package com.tiancheng.ms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.dto.PageRequestWrapper;
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
    private ProdProcRelatMapper prodProcRelatMapper;
    @Autowired
    private ProdMaterialRelatMapper prodMaterialRelatMapper;
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

        if (!CollectionUtils.isEmpty(detailParam.getProductProcessParams())) {
            ProductEntity finalProductEntity = productEntity;
            detailParam.getProductProcessParams().stream().forEach(entity -> {
                entity.setProductId(finalProductEntity.getId());
            });
            prodProcRelatMapper.insertList(detailParam.getProductProcessParams());
        }

        if (!CollectionUtils.isEmpty(detailParam.getProductMaterialParams())) {
            ProductEntity finalProductEntity1 = productEntity;
            detailParam.getProductMaterialParams().stream().forEach(entity -> {
                entity.setProductId(finalProductEntity1.getId());
            });
            prodMaterialRelatMapper.insertList(detailParam.getProductMaterialParams());
        }

        if (!CollectionUtils.isEmpty(detailParam.getRuleParams())) {
            ProductEntity finalProductEntity2 = productEntity;
            detailParam.getRuleParams().stream().forEach(entity -> {
                entity.setProdId(finalProductEntity2.getId());
            });
            rulesMapper.insertList(detailParam.getRuleParams());
        }
    }

    public Page<ProductEntity> pageQryProduct(PageRequestWrapper<ProductQueryParam> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        List<ProductEntity> entities = new ArrayList<>();
        Example example = new Example(ProductEntity.class);
        if (pageRequest.getQueryParam() != null && !StringUtils.isEmpty(pageRequest.getQueryParam().getName().trim())) {
            example.createCriteria().andLike(ExampleConstant.PRODUCT_NAME, pageRequest.getQueryParam().getName().trim());
            entities =  productMapper.selectByExample(example);
        } else {
            entities = productMapper.selectAll();
        }
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
        Example example = new Example(ProductEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo(ExampleConstant.PRODUCT_CODE,detailParam.getProductParam().getCode());
        ProductEntity productEntity = productMapper.selectOneByExample(example);
        if (productEntity != null && productEntity.getId() != detailParam.getProductParam().getId()) {
            throw new BusinessException(ErrorCode.FAIL, "产品编码重复");
        }
        productEntity = productMapper.selectByPrimaryKey(detailParam.getProductParam().getId());
        if (productEntity == null) {
            throw new BusinessException(ErrorCode.FAIL, "产品不存在，请重试！");
        }
        BeanUtils.copyProperties(detailParam.getProductParam(), productEntity);
        productMapper.updateByPrimaryKeySelective(productEntity);

        Example example1 = new Example(ProductProcessEntity.class);
        example1.createCriteria().andEqualTo(ExampleConstant.RELATION_PROD_ID, productEntity.getId());
        List<Integer> existProcIds = prodProcRelatMapper.selectByExample(example1).stream()
                .map(entity -> entity.getProcessId())
                .collect(Collectors.toList());
        List<Integer> addProcIds = detailParam.getProductProcessParams().stream()
                .map(entity -> entity.getProcessId())
                .collect(Collectors.toList());
        List<Integer> removeProcIds = new ArrayList<>(existProcIds);
        removeProcIds.removeAll(addProcIds);
        addProcIds.removeAll(existProcIds);
        if (!CollectionUtils.isEmpty(removeProcIds)) {
            prodProcRelatMapper.deleteByIds(StringUtils.arrayToCommaDelimitedString(removeProcIds.toArray()));
        }
        if (!CollectionUtils.isEmpty(addProcIds)) {
            List<ProductProcessEntity> prodProcRelationEntities = addProcIds.stream().map(id -> {
                return new ProductProcessEntity(detailParam.getProductParam().getId(), id);
            }).collect(Collectors.toList());
            prodProcRelatMapper.insertList(prodProcRelationEntities);
        }

        Example example2 = new Example(ProductMaterial.class);
        example2.createCriteria().andEqualTo(ExampleConstant.RELATION_PROD_ID, productEntity.getId());
        prodMaterialRelatMapper.deleteByExample(example2);
        if (!CollectionUtils.isEmpty(detailParam.getProductMaterialParams())) {
            detailParam.getProductMaterialParams().stream().forEach(entity -> {
                entity.setProductId(detailParam.getProductParam().getId());
            });
            prodMaterialRelatMapper.insertList(detailParam.getProductMaterialParams());
        }

        Example example3 = new Example(RulesEntity.class);
        example3.createCriteria().andEqualTo(ExampleConstant.RELATION_PROD_ID, productEntity.getId());
        rulesMapper.deleteByExample(example3);
        if (!CollectionUtils.isEmpty(detailParam.getRuleParams())) {
            detailParam.getRuleParams().stream().forEach(entity -> {
                entity.setProdId(detailParam.getProductParam().getId());
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
}
