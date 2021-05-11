package com.plawande.springsecurityjwt.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
