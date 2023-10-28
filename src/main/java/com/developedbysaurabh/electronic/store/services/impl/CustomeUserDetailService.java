package com.developedbysaurabh.electronic.store.services.impl;

import com.developedbysaurabh.electronic.store.entities.User;
import com.developedbysaurabh.electronic.store.exceptions.ResourceNotFoundException;
import com.developedbysaurabh.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomeUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given Email !"));
        return user;
    }
}
