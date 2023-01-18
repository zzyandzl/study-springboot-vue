package com.zl.common;

public enum Constants {
    CODE_0("0"),//成功
    CODE_101("101"),//失败
    CODE_400("400"),// 参数错误
    CODE_401("401"),// 权限不足
    CODE_402("402"),// 系统错误
    CODE_600("600"); // 其他业务异常

    private String code;

    private Constants(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
