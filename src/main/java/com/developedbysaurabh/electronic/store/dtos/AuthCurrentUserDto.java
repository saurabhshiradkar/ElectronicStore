package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCurrentUserDto {

    private String userId;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String about;
    private String imageName;
//    private List<OrderDto> orders = new ArrayList<>();
    private boolean enabled;
    private String username;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean accountNonExpired;
    private List<GrantedAuthority> authorities;



}
