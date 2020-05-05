package com.tiancheng.ms.common.dto;
import com.tiancheng.ms.constant.ErrorCode;

public class AjaxResult<T> {
    private int code;
    private T data;
    private String msg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> AjaxResult success(T data) {
        AjaxResult<T> ajaxResult = new AjaxResult<>();
        ajaxResult.setCode(ErrorCode.SUCCESS.getCode());
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static <T> AjaxResult fail(String msg) {
        AjaxResult<T> ajaxResult = new AjaxResult<>();
        ajaxResult.setCode(ErrorCode.FAIL.getCode());
        ajaxResult.setMsg(msg);
        return ajaxResult;
    }


    public static <T> AjaxResult notAuth() {
        AjaxResult<T> ajaxResult = new AjaxResult<>();
        ajaxResult.setCode(ErrorCode.FAIL_AUTH.getCode());
        ajaxResult.setMsg(ErrorCode.FAIL_AUTH.getMsg());
        return ajaxResult;
    }
}
