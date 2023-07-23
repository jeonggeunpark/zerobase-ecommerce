package com.zerobase.commerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private  static  final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; //1시간
    private static final String KEY_ROLES = "roles";

    @Value("{spring.jwt.secret}")
    private String secretKey;

    public String generateToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);
        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey) //사용할 알고리즘과 비밀키
                .compact();
    }

    public String getUsername(String token){
        return this.parseClaims(token).getSubject();
    }
    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token){
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return  e.getClaims();
        }
        }
}