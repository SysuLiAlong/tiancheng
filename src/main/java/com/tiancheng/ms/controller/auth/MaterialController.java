package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.aop.DeleteType;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.dto.SelectOption;
import com.tiancheng.ms.common.enums.DeleteTypeEnum;
import com.tiancheng.ms.dto.MaterialDTO;
import com.tiancheng.ms.dto.param.MaterialParam;
import com.tiancheng.ms.dto.param.MaterialQueryParam;
import com.tiancheng.ms.dto.param.MaterialTypeParam;
import com.tiancheng.ms.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @RequestMapping("/add")
    public void addMaterial(@RequestBody MaterialParam param) {
        materialService.addMaterial(param);
    }

    @RequestMapping("/page_qry")
    public Page<MaterialDTO> pageQryMaterial(@RequestBody MaterialQueryParam queryParam) {
        return materialService.pageQueryMaterial(queryParam);
    }

    @RequestMapping("/list")
    public List<MaterialDTO> listMaterial(@RequestParam(value = "code",required = false) String code) {
        return materialService.listMaterial(code);
    }

    @DeleteType(value = DeleteTypeEnum.MATERIAL, id = "id")
    @PostMapping("/delete/{id}")
    public void deleteMaterial(@PathVariable Integer id) {
        materialService.deleteMaterial(id);
    }



    @RequestMapping("/type/add")
    public void addMaterialType(@RequestBody MaterialTypeParam param) {
        materialService.addMaterialType(param);
    }

    @RequestMapping(value = "/type/options")
    List<SelectOption> typeOptions() {
        return materialService.typeOptions();
    }

    @RequestMapping(value = "/options")
    List<SelectOption> options(@RequestParam Integer typeId) {
        return materialService.options(typeId);
    }




}
