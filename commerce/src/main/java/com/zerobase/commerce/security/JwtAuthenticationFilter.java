package com.zerobase.commerce.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOEKEN_HEADER = "Authorization";
    public static final String TOEKEN_PREFIX = "bearer";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = this.resolveTokenFromRequest(request);

    if(StringUtils.hasText(token) && this.tokenProvider.validateToken(token)){//토큰 유효성 검증
        Authentication auth = this.tokenProvider.getAuthenticatrion(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request,response);
    }
    private String resolveTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(TOEKEN_HEADER);

        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOEKEN_PREFIX)){
            return token.substring(TOEKEN_PREFIX.length());

        }
    }
}
