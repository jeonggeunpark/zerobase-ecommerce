package com.zerobase.commerce.controller;

import com.zerobase.commerce.model.Auth;
import com.zerobase.commerce.security.TokenProvider;
import com.zerobase.commerce.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup") //회원가입을 위한 API
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
        var result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }
    @PostMapping ("/signin")//로그인을 위한 API
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn requset){
        //패스워드 검증
        var member = this.memberService.authenticate(requset);
        //인증이 되었을때 토큰반환
        var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
        return ResponseEntity.ok(token);
    }
}
