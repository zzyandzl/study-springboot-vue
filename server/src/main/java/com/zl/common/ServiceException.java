package com.zl.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常基础类
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceException extends RuntimeException{

    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
