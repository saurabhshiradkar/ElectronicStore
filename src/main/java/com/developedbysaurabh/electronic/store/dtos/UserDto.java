package com.developedbysaurabh.electronic.store.dtos;

import com.developedbysaurabh.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {


    private String userId;

    @Size(min = 3, max = 30, message = "Invalid Name !")
    private String name;

//    @Email(message = "Invalid User Email !")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email!")
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
    private String imageName;
}
