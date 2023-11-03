package com.developedbysaurabh.electronic.store.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeController {
    @GetMapping
    public String testing(){
        return "<h1>Welcome To Electronic Store</h1>";
    }
}
