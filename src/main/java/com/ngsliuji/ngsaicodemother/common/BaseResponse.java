package com.ngsliuji.ngsaicodemother.common;

import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/*一般情况下，每个后端接口都؜؜要؜返回调用码、数؜؜据、؜调用信息等，前端可以根据这些信息进行相应的处理。

我们可以封装统一的响应结果类，便于؜前端统一获取这些信息。

通用响应类：*/
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

