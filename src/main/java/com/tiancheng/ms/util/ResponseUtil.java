package com.tiancheng.ms.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * response的工具类
 *
 * @author gongyi
 * Email; 76429197@qq.com
 * Date: 2018-10-24
 */
@Slf4j
@SuppressWarnings("all")
public class ResponseUtil {
    public static void write(HttpServletResponse resp, String result) {
        try {
            //这句话的意思，是让浏览器用utf8来解析返回的数据
            resp.setHeader("Content-type", "application/json;charset=UTF-8");
            OutputStream ps = resp.getOutputStream();
            //这句话的意思，使得放入流的数据是utf8格式
            ps.write(result.getBytes(StandardCharsets.UTF_8));
            ps.flush();
        } catch (IOException e) {
            log.error("response 写出异常：" + result, e);
        }
    }
}
