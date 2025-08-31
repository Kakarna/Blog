package com.easy.controller;

import com.easy.enums.ResponseCodeEnum;
import com.easy.entity.vo.ResponseVO;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public class ABaseController {

    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";

    // 成功响应
    protected <T> ResponseVO<T> getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    // 统一错误响应
    protected <T> ResponseVO<T> getErrorResponseVO(int code, String info) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUS_ERROR);
        responseVO.setCode(code);
        responseVO.setInfo(info);
        responseVO.setData(null);
        return responseVO;
    }

    // 检查是否是 admin
    protected boolean checkAdmin(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        return claims != null && "admin".equals(claims.get("role"));
    }

    // 检查是否登录
    protected boolean checkLogin(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        return claims != null;
    }
}
