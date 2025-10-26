package com.example.knowledge_service.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.knowledge_service.note.service.NoteService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class NotivaHomeController{

    private final NoteService noteService;
    
    /**
     * 노티바 로그인 전 홈 컨트롤러
     * @return templates /home/home
     */
    @GetMapping("/home")
    public String home() {
        
        return "home/home";
    }
    
    /**
     * 
     * @param id 유저아이디 사용자 id 필요
     * @param model 뷰에 전달할 데이터 노트리스트와 노트리스트들의 생성 변경시간, 투두리스트 기간
     * @return templates /home/notiva
     */
    @GetMapping("/Notiva")
    public String getNoteCalendarTodoHomePage(@RequestParam(value = "userId") Long userId, Model model) {

        // 노트 리스트
        model.addAttribute("noteList", noteService.getListNotes(userId));

        return "/home/notiva";
    }
}