package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.dto.AlarmDTO;
import com.tiancheng.ms.dto.param.AlarmQueryParam;
import com.tiancheng.ms.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @RequestMapping("/page_qry")
    public Page<AlarmDTO> pageQry(@RequestBody AlarmQueryParam queryParam) {
        return alarmService.pageQry(queryParam);
    }
}
