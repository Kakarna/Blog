package com.easy.utils;

import com.easy.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    private final JwtProperties jwtProperties;
    private final Key key;

    @Autowired
    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtProperties.getSecret().getBytes()));
    }

    public String generateToken(Map<String, Object> claims, Long expireMillis) {
        long expire = (expireMillis != null) ? expireMillis : jwtProperties.getExpire();
        return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + expire)).setIssuedAt(new Date()).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String generateToken(Map<String, Object> claims) {
        return generateToken(claims, null);
    }

    public Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}