package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.aop.DeleteType;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.enums.DeleteTypeEnum;
import com.tiancheng.ms.constant.UserRoleConstant;
import com.tiancheng.ms.dto.ProduceDTO;
import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.dto.ProduceProductDTO;
import com.tiancheng.ms.dto.ProduceProductDetailDTO;
import com.tiancheng.ms.dto.param.ProduceMsgParam;
import com.tiancheng.ms.dto.param.ProduceParam;
import com.tiancheng.ms.dto.param.ProduceProcessAndMsgParam;
import com.tiancheng.ms.dto.param.ProduceQueryParam;
import com.tiancheng.ms.entity.ProduceMsgEntity;
import com.tiancheng.ms.service.ProduceService;
import com.tiancheng.ms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produce")
public class ProduceController {

    @Autowired
    private ProduceService produceService;

    @Autowired
    UserRoleConstant userRoleConstant;

    @PostMapping("/page_qry")
    public Page<ProduceDTO> pageQryProduce(@RequestBody ProduceQueryParam queryParam) {
        String currentUserRole = ContextHolder.getUser().getRealName();
        if (userRoleConstant.commonUser.equals(currentUserRole)) {
            return produceService.pageQryProduceForCommonUser(queryParam);
        } else {
            return produceService.pageQryProduceForAdmin(queryParam);
        }
    }

    @GetMapping("/{produceId}")
    public List<ProduceProductDTO> listProduceProduct(@PathVariable Integer produceId) {
        return produceService.listProduceProduct(produceId);
    }

    @PostMapping("/add")
    public void addProduce(@RequestBody ProduceParam produceParam) {
        produceService.addProduce(produceParam);
    }

    @PostMapping("/msg/add")
    public void addProduceMsg(@RequestBody ProduceMsgParam param) {
        produceService.addProduceMsg(param);
    }

    @GetMapping("/process/{produce_product_id}")
    public List<ProduceProcessDTO> listProduceProcess(@PathVariable("produce_product_id") Integer produceProductId) {
        return produceService.listProduceProcess(produceProductId);
    }

    @GetMapping("/msg/{produce_product_id}")
    public List<ProduceMsgEntity> listProduceMsg(@PathVariable("produce_product_id") Integer produceProductId) {
        return produceService.listProduceMsg(produceProductId);
    }

    @GetMapping("/detail/{produce_product_id}")
    public ProduceProductDetailDTO getProduceDetail(@PathVariable("produce_product_id") Integer produceProductId) {
        return produceService.getProduceProductDetail(produceProductId);
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

    @GetMapping("/process/last/{produce_product_id}")
    public ProduceProcessDTO getLastProduceProcess(@PathVariable("produce_product_id") Integer produceProductId) {
        return BeanUtils.copy(produceService.getLastProcess(produceProductId), ProduceProcessDTO.class);
    }

    @GetMapping("/process/current/{produce_product_id}")
    public ProduceProcessDTO getCurrentProduceProcess(@PathVariable("produce_product_id") Integer produceProductId) {
        return BeanUtils.copy(produceService.getCurrentProcess(produceProductId), ProduceProcessDTO.class);
    }

    @GetMapping("/process/next/{produce_product_id}")
    public ProduceProcessDTO getNextProduceProcess(@PathVariable("produce_product_id") Integer produceProductId) {
        return BeanUtils.copy(produceService.getNextProcess(produceProductId), ProduceProcessDTO.class);
    }

    @DeleteType(value = DeleteTypeEnum.PRODUCE, id = "id")
    @PostMapping("/delete/{id}")
    public void deleteProduce(@PathVariable Integer id) {
        produceService.deleteProduce(id);
    }

}
