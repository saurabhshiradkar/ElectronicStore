package com.developedbysaurabh.electronic.store.entities;


import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;

    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email",unique = true)
    private String email;

    @Column(name = "user_password")
    private String password;

    private String gender;

    @Column(length = 5000)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;



}
