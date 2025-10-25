package com.example.knowledge_service.note.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.knowledge_service.note.dto.NoteDTO;
import com.example.knowledge_service.note.service.NoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteCRUDController {
    
    private final NoteService noteService;

    /**
     * 노트 작성 페이지 조회
     * @param model 뷰에 전달할 데이터 모델
     * @param userId 사용자 ID
     * @return 노트 작성 페이지 뷰
     */
    @GetMapping("/new")
    public String getNewNotePage(Model model, @RequestParam(value = "userId") Long userId) {

        model.addAttribute("newNote", new NoteDTO());
        model.addAttribute("userId", userId);

        return "note/write";
    }

    /**
     * 노트 생성 처리
     * @param noteDTO 노트 데이터
     * @param userId 사용자 ID
     * @return 노트 리스트 페이지로 리다이렉트
     */
    @PostMapping("/new")
    public String postAddNewNote(@ModelAttribute NoteDTO noteDTO, @RequestParam(value = "userId") Long userId) {
        
        noteService.createNewNote(noteDTO, userId);

        return "redirect:/note/list?userId=" + userId;
    }
}
