package com.developedbysaurabh.electronic.store.services;

import com.developedbysaurabh.electronic.store.dtos.UserDto;
import com.developedbysaurabh.electronic.store.entities.User;

import java.util.List;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);

    //update

    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    //get all users
    List<UserDto> getAllUser();

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);


   //other user specific features

}
