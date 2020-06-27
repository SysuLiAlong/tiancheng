package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.entity.ProduceEntity;
import com.tiancheng.ms.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce")
public class ProduceController {

    @Autowired
    private ProduceService produceService;

    public void addProduce(ProduceEntity produceEntity) {

    }
}
