package com.easy.entity.vo;

import com.easy.enums.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseVO<T> {
    // Getter 和 Setter 保留
    private String status;
    private Integer code;
    private String info;
    private T data;

    // ✅ 构造静态方法（推荐使用）
    public static <T> ResponseVO<T> ok(T data) {
        ResponseVO<T> result = new ResponseVO<>();
        result.setStatus("success");
        result.setCode(ResponseCodeEnum.CODE_200.getCode());
        result.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> ResponseVO<T> error(String msg) {
        ResponseVO<T> result = new ResponseVO<>();
        result.setStatus("error");
        result.setCode(ResponseCodeEnum.CODE_500.getCode());
        result.setInfo(msg);
        result.setData(null);
        return result;
    }

    public static <T> ResponseVO<T> error(ResponseCodeEnum codeEnum) {
        ResponseVO<T> result = new ResponseVO<>();
        result.setStatus("error");
        result.setCode(codeEnum.getCode());
        result.setInfo(codeEnum.getMsg());
        result.setData(null);
        return result;
    }
    @Override
    public String toString() {
        return "ResponseVO{" +
                "code=" + code +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }

}
