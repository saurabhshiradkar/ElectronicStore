package com.developedbysaurabh.electronic.store.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Superuser {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String about;
    private String imageName;

}