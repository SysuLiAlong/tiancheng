package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.dto.PageRequestWrapper;
import com.tiancheng.ms.dto.ProductDetailDTO;
import com.tiancheng.ms.dto.param.ProductDetailParam;
import com.tiancheng.ms.dto.param.ProductQueryParam;
import com.tiancheng.ms.entity.ProductEntity;
import com.tiancheng.ms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public void addProduct(@RequestBody ProductDetailParam detailParam) {
        productService.addProduct(detailParam);
    }

    @PostMapping("/update")
    public void updateProduct(@RequestBody ProductDetailParam detailParam) {
        productService.updateProduct(detailParam);
    }

    @PostMapping("/page")
    public Page<ProductEntity> pageQry(@RequestBody PageRequestWrapper<ProductQueryParam> pageRequest) {
        return productService.pageQryProduct(pageRequest);
    }

    @GetMapping("{productId}")
    public ProductDetailDTO detailProduct(@PathVariable Integer productId) {
        return productService.detail(productId);
    }

    @PostMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
    }
}
