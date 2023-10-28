package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {

    private String email;
    private String password;
}
