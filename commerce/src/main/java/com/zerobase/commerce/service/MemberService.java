package com.zerobase.commerce.service;

import com.zerobase.commerce.model.Auth;
import com.zerobase.commerce.persist.MemberRepositoty;
import com.zerobase.commerce.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepositoty memberRepositoty;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  this.memberRepositoty.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));

    }
    public MemberEntity register(Auth.SignUp member){ //회원가입
        boolean exists =this.memberRepositoty.existByUsername(member.getUsername());
        if(exists){
            throw new RuntimeException("이미 사용 중인 아이디 입니다.");
        }
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result =this.memberRepositoty.save(member.toEntity());
        return result;
    }
    public  MemberEntity authenticate(Auth.SignIn member){ //로그인
        var user = this.memberRepositoty.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));
        if(this.passwordEncoder.matches(member.getPassword(),user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.")
        }
        return user;
    }
}
