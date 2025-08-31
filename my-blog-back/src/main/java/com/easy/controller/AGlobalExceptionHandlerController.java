package com.easy.controller;

import com.easy.enums.ResponseCodeEnum;

import com.easy.entity.vo.ResponseVO;

import com.easy.exception.BusinessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
@RestControllerAdvice
public class AGlobalExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);
    private static final String STATUS_ERROR = "error";

    @ExceptionHandler(value = Exception.class)
    public ResponseVO<Object> handleException(Exception e, HttpServletRequest request) {
        logger.error("Exception occurred, URL: {}", request.getRequestURL(), e);

        ResponseVO<Object> ajaxResponse = new ResponseVO<>();

        if (e instanceof NoHandlerFoundException) {
            // 404 Not Found
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
        } else if (e instanceof BusinessException) {
            // 业务异常
            BusinessException biz = (BusinessException) e;
            ajaxResponse.setCode(biz.getCode());
            ajaxResponse.setInfo(biz.getMessage());
        } else if (e instanceof BindException) {
            // 参数绑定错误
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_600.getMsg());
        } else if (e instanceof DuplicateKeyException) {
            // 主键冲突
            ajaxResponse.setCode(ResponseCodeEnum.CODE_601.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_601.getMsg());
        } else {
            // 500 Server Error
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        }

        ajaxResponse.setStatus(STATUS_ERROR);
        return ajaxResponse;
    }
}
