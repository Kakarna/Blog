package com.easy.enums;

public enum ResponseCodeEnum {
    // 枚举值（注意顺序和语法）
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址不存在"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_500(500, "服务器返回错误，请联系管理员");

    // 成员变量
    private final Integer code;
    private final String msg;

    // 构造方法
    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // Getter 方法
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
