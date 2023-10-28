package com.developedbysaurabh.electronic.store.controllers;

import com.developedbysaurabh.electronic.store.dtos.AuthCurrentUserDto;
import com.developedbysaurabh.electronic.store.dtos.JwtRequest;
import com.developedbysaurabh.electronic.store.dtos.JwtResponse;
import com.developedbysaurabh.electronic.store.dtos.UserDto;
import com.developedbysaurabh.electronic.store.exceptions.BadApiRequestException;
import com.developedbysaurabh.electronic.store.security.JwtHelper;
import com.developedbysaurabh.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserDetailsService userDetailsService;
    private ModelMapper mapper;
    private AuthenticationManager authenticationManager;

    private JwtHelper jwtHelper;
    private UserService userService;

    @Autowired
    public AuthController(UserDetailsService userDetailsService, ModelMapper mapper, AuthenticationManager authenticationManager, JwtHelper jwtHelper, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        String email = request.getEmail();
        String password = request.getPassword();
        this.doAuthenticate(email,password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = this.jwtHelper.generateToken(userDetails);

        UserDto userDto = mapper.map(userDetails,UserDto.class);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtTolken(token)
                .user(userDto)
                .build();
        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,password);

        try
        {
            authenticationManager.authenticate(authentication);
        }
        catch (BadCredentialsException e)
        {
            e.printStackTrace();
            throw new BadApiRequestException("Invalid Username or Password ! ");
        }
    }


    @GetMapping("/current")
    public ResponseEntity<AuthCurrentUserDto> getCurrentUser(Principal principal){
        String name = null;
        if (principal.getName()!= null)
        {
             name = principal.getName();
        }
        else {
            throw  new BadApiRequestException("ACCESS DENIED!");
        }
//        return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(name),UserDto.class),HttpStatus.OK);
        return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(name), AuthCurrentUserDto.class),HttpStatus.OK);
    }
}
