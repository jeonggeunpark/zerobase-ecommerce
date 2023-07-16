package com.zerobase.commerce.domain;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AddUser {
    private String email;
    private String name;
    private String password;
    private LocalDate birth;
    private String phoneNumber;

}
