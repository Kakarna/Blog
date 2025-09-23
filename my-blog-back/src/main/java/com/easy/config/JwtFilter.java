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

/**
 * JWT 权限过滤器
 */
@Component
public class JwtFilter implements Filter {

    /**
     * 需要鉴权的接口（增删改类接口）
     * 注意：这里只是“需要鉴权”的 URL，并不区分 admin/user，
     * 后面会结合 token 中的 role / isPublic 来判断具体权限。
     */
    private static final List<String> PROTECTED_URLS = Arrays.asList(
            "/techSection/privateUser",
            "/techSection/add",
            "/techSection/update/*",
            "/techSection/delete/*",
            "/techNote/add",
            "/techNote/update",
            "/techNote/delete",
        /*    "/techNote/getPrivateNotes",*/
            "/studyRecord/loadPrivateDataList",
            "/studyRecord/add",
            "/studyRecord/update",
            "/studyRecord/delete",
           /* "/project/privateProjects",*/
            "/project/addWithFiles",
            "/projectFile/deleteByProjectFile/*",
            "/project/delete/*"
    );

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        // 判断是否需要鉴权
        if (needsAuth(uri)) {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Claims claims;
                try {
                    claims = jwtUtils.parseToken(token);
                    req.setAttribute("claims", claims);
                } catch (ExpiredJwtException e) {
                    sendError(resp, 401, "Token 已过期");
                    return;
                } catch (JwtException e) {
                    sendError(resp, 401, "Token 无效");
                    return;
                }

                // 从 token 中取出角色 & isPublic & 用户id
                String role = (String) claims.get("role");
                Integer userId = (Integer) claims.get("id");
                Integer isPublic = claims.get("isPublic") != null ? (Integer) claims.get("isPublic") : 0;

                // 如果是 admin → 放行
                if ("admin".equals(role)) {
                    chain.doFilter(request, response);
                    return;
                }

                // 普通用户 & 公共账号逻辑（支持大小写）
                if ("user".equalsIgnoreCase(role)) {
                    req.setAttribute("userId", userId);
                    req.setAttribute("isPublic", isPublic);
                    chain.doFilter(request, response);
                    return;
                }

                // 未知角色 → 拒绝
                sendError(resp, 403, "无权限访问");
                return;
            } else {
                sendError(resp, 401, "请登录");
                return;
            }
        }

        // 不需要鉴权的接口，直接放行
        chain.doFilter(request, response);
    }

    /**
     * 判断接口是否需要鉴权
     */
    private boolean needsAuth(String uri) {
        for (String url : PROTECTED_URLS) {
            if (url.endsWith("/*")) {
                String prefix = url.substring(0, url.length() - 1);
                if (uri.startsWith(prefix)) return true;
            } else {
                if (uri.equals(url)) return true;
            }
        }
        return false;
    }

    /**
     * 返回错误信息
     */
    private void sendError(HttpServletResponse resp, int code, String msg) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"status\":\"error\",\"code\":" + code + ",\"info\":\"" + msg + "\"}");
    }
}
