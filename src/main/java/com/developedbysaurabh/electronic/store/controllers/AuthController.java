package com.developedbysaurabh.electronic.store.controllers;

import com.developedbysaurabh.electronic.store.dtos.AuthCurrentUserDto;
import com.developedbysaurabh.electronic.store.dtos.JwtRequest;
import com.developedbysaurabh.electronic.store.dtos.JwtResponse;
import com.developedbysaurabh.electronic.store.dtos.UserDto;
import com.developedbysaurabh.electronic.store.entities.User;
import com.developedbysaurabh.electronic.store.exceptions.BadApiRequestException;
import com.developedbysaurabh.electronic.store.security.JwtHelper;
import com.developedbysaurabh.electronic.store.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserDetailsService userDetailsService;
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AuthenticationManager authenticationManager;

    private JwtHelper jwtHelper;
    private UserService userService;

    @Value("${googleClientId}")
    private String googleClientId;

    @Value("${newPassword}")
    private String newPassword;

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


    //login with google api

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {

        //get the id token from request
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(),idToken);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload : {}",payload);

        String email = payload.getEmail();

        User user =null;

        user = userService.findUserByEmailOptional(email).orElse(null);

        if(user == null)
        {
            //create new user

            user = this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
        }

        ResponseEntity<JwtResponse> jwtResponseResponseEntity = this.login(JwtRequest.builder().email(email).password(newPassword).build());

        return jwtResponseResponseEntity;
    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);
        return this.mapper.map(user,User.class);

    }

}
