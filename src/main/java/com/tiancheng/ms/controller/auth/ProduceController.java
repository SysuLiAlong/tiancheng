package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.dto.ProduceDetailDTO;
import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.dto.param.ProduceParam;
import com.tiancheng.ms.dto.param.ProduceProcessAndMsgParam;
import com.tiancheng.ms.dto.param.ProduceQueryParam;
import com.tiancheng.ms.entity.ProduceMsgEntity;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import com.tiancheng.ms.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produce")
public class ProduceController {

    @Autowired
    private ProduceService produceService;

    @PostMapping("/page_qry")
    public Page<ProduceDetailDTO> pageQryProduce(@RequestBody ProduceQueryParam queryParam) {
        return produceService.pageQryProduce(queryParam);
    }

    @PostMapping("/add")
    public void addProduce(@RequestBody ProduceParam produceParam) {
        produceService.addProduce(produceParam);
    }

    @PostMapping("/msg/add")
    public void addProduceMsg(@RequestBody ProduceMsgEntity produceMsgEntity) {
        produceService.addProduceMsg(produceMsgEntity);
    }

    @GetMapping("/process/{produceId}")
    public List<ProduceProcessDTO> listProduceProcess(@PathVariable Integer produceId) {
        return produceService.listProduceProcess(produceId);
    }

    @GetMapping("/msg/{produceId}")
    public List<ProduceMsgEntity> listProduceMsg(@PathVariable Integer produceId) {
        return produceService.listProduceMsg(produceId);
    }

    @GetMapping("/detail/{produceId}")
    public ProduceDetailDTO getProduceDetail(@PathVariable Integer produceId) {
        return produceService.getProduceDetail(produceId);
    }

    @PostMapping("/process/accept")
    public void accetpProcess(@RequestBody ProduceProcessAndMsgParam param) {
        produceService.acceptProcess(param);
    }

    @PostMapping("/process/reject")
    public void rejectProcess(@RequestBody ProduceProcessAndMsgParam param) {
        produceService.rejectProcess(param);
    }

    @PostMapping("/process/transmit")
    public void transmitProcess(@RequestBody ProduceProcessAndMsgParam param) {
        produceService.transmitProcess(param);
    }

    @GetMapping("/process/last/{produceId}")
    public ProduceProcessEntity getLastProduceProcess(@PathVariable Integer produceId) {
        return produceService.getLastProcess(produceId);
    }

    @GetMapping("/process/current/{produceId}")
    public ProduceProcessEntity getCurrentProduceProcess(@PathVariable Integer produceId) {
        return produceService.getCurrentProcess(produceId);
    }

    @GetMapping("/process/next/{produceId}")
    public ProduceProcessEntity getNextProduceProcess(@PathVariable Integer produceId) {
        return produceService.getNextProcess(produceId);
    }

    @PostMapping("/delete/{produceId}")
    public void deleteProduce(@PathVariable Integer produceId) {
        produceService.deleteProduce(produceId);
    }
}
