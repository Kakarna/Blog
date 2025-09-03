package com.easy.config;

import com.easy.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter implements Filter {
    private static final List<String> ADMIN_URLS = Arrays.asList("/techSection/add", "/techSection/update/*", "/techSection/delete/*", "/techNote/add", "/techNote/update", "/techNote/delete", "/studyRecord/add", "/studyRecord/update", "/studyRecord/delete", "/project/addWithFiles","/projectFile/deleteByProjectFile/*","/project/delete/*");
    private final JwtUtils jwtUtils;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (needsAdmin(uri)) {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Claims claims;
                try {
                    claims = jwtUtils.parseToken(token);
                    req.setAttribute("claims", claims);
                } catch (ExpiredJwtException e) {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write("{\"status\":\"error\",\"code\":401,\"info\":\"Token 已过期\"}");
                    return;
                } catch (JwtException e) {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write("{\"status\":\"error\",\"code\":401,\"info\":\"Token 无效\"}");
                    return;
                }
                String role = (String) claims.get("role");
                if (!"admin".equals(role)) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write("{\"status\":\"error\",\"code\":403,\"info\":\"无权限访问\"}");
                    return;
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"status\":\"error\",\"code\":401,\"info\":\"请登录\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean needsAdmin(String uri) {
        for (String adminUrl : ADMIN_URLS) {
            if (adminUrl.endsWith("/*")) {
                String prefix = adminUrl.substring(0, adminUrl.length() - 1);
                if (uri.startsWith(prefix)) return true;
            } else {
                if (uri.equals(adminUrl)) return true;
            }
        }
        return false;
    }
}