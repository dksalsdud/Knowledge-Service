package com.example.knowledge_service.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController{
    
    //notiva home controller
    
    @GetMapping("/home")
    public String home() {
        
        return "home/home";
    }
}