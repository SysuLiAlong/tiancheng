package com.tiancheng.ms.common.exception;

import com.tiancheng.ms.constant.ErrorCode;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


    public BusinessException(ErrorCode errorCode, String message) {
        super(message, null);
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (!StringUtils.isEmpty(message)) {
            return message;
        }
        return errorCode.getMsg();
    }
}
