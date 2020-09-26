package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.aop.DeleteType;
import com.tiancheng.ms.common.enums.DeleteTypeEnum;
import com.tiancheng.ms.dto.ProcesssDTO;
import com.tiancheng.ms.dto.param.ProcessParam;
import com.tiancheng.ms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @RequestMapping(value = "/add")
    public void addProcess(@RequestBody ProcessParam param) {
        processService.addProcess(param);
    }

    @PostMapping(value = "update")
    public void updateProcess(@RequestBody ProcessParam param) {
        processService.updateProcess(param);
    }

    @RequestMapping("/list")
    public List<ProcesssDTO> listProcess() {
        return processService.listProcess();
    }

    @DeleteType(value = DeleteTypeEnum.PROCESS, id = "id")
    @RequestMapping("/delete/{id}")
    public void deleteProcess(@PathVariable(value = "id") Integer id) {
        processService.deleteProcess(id);
    }

    @RequestMapping("/exchange/priority")
    public void exchangePriority(@RequestParam(name = "first") Integer first, @RequestParam(name = "second") Integer second) {
        processService.exchangePriority(first, second);
    }

    @GetMapping("/detail/{processId}")
    public ProcesssDTO processDetail(@PathVariable Integer processId) {
        return processService.processDetail(processId);
    }

}
