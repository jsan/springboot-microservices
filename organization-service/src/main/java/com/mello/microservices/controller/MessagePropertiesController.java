package com.mello.microservices.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagePropertiesController
{
    @Value("${spring.boot.message}")
    private String message;

    @GetMapping("message")
    public String getMessage (){
        return message;
    }
}
