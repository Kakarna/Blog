package com.easy.config;

import com.easy.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class VisitFilter implements Filter {

    @Autowired
    private VisitService visitService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String ip = req.getRemoteAddr();
        String userAgent = req.getHeader("User-Agent");

        // 只统计页面请求，不统计接口调用
        String uri = req.getRequestURI();
        if (!uri.startsWith("/api")) {
            visitService.addVisit(ip, userAgent);
        }

        chain.doFilter(request, response);
    }
}
