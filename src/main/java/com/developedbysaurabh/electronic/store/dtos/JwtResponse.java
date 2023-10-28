package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String jwtTolken;
    private UserDto user;
}
