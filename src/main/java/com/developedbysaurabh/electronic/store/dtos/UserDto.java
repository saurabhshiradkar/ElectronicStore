package com.developedbysaurabh.electronic.store.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {


    @Schema( name = "userId", required = true,accessMode = Schema.AccessMode.READ_ONLY, description = "USER ID ")
    private String userId;


    @Size(min = 3, max = 30, message = "Invalid Name !")
    private String name;

//    @Schema( name = "email", required = true,accessMode = Schema.AccessMode.READ_ONLY, description = "email of new user !!")
//    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email!")
    @Email(message = "Invalid User Email !")
    @NotBlank(message = "Email is Required!")
    private String email;

    @NotBlank(message = "Password is Required!")
    private String password;

    @Size(min = 4, max = 12, message = "Invalid Gender")
    private String gender;

    private String about;

    //@Pattern

    //Custom  Validator
    //@ImageNameValid
    @Schema( name = "imageName", required = true,accessMode = Schema.AccessMode.READ_ONLY, description = "IMAGE NAME ")
    private String imageName;

    @Schema( name = "roles", required = true,accessMode = Schema.AccessMode.READ_ONLY, description = "ROLES ")
    private Set<RoleDto> roles = new HashSet<>();
}
