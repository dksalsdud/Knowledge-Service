package com.example.knowledge_service.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotivaHomeController{
    
    /**
     * 노티바 로그인 전 홈 컨트롤러
     * @return templates /home/home
     */
    @GetMapping("/home")
    public String home() {
        
        return "home/home";
    }
}