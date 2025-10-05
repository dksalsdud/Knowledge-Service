package com.example.knowledge_service.note.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.example.knowledge_service.note.dto.NoteDTO;
import com.example.knowledge_service.note.service.NoteService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;




@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteservice;

    /**
     * 노트 홈 페이지 조회
     * @param model 뷰에 전달할 데이터 모델
     * @param userId 사용자 ID (현재는 파라미터, 추후 세션/인증으로 대체 필요)
     * @return 노트 홈 페이지 뷰
     */
    @GetMapping()
    public String getNoteHomePage(Model model, @RequestParam(value = "userId") Long userId) {

        model.addAttribute("listNotes", noteservice.getListNotes(userId));
        model.addAttribute("userId", userId);

        return "note/home";
    }

    /**
     * 노트 리스트 페이지 조회
     * @param model 뷰에 전달할 데이터 모델
     * @param userId 사용자 ID
     * @return 노트 리스트 페이지 뷰
     */
    @GetMapping("/list")
    public String getNoteListPage(Model model, @RequestParam(value = "userId") Long userId) {

        model.addAttribute("listNotes", noteservice.getListNotes(userId));
        model.addAttribute("userId", userId);

        return "note/list";
    }


    /**
     * 노트 상세 페이지 조회
     * @param id 노트 ID
     * @param model 뷰에 전달할 데이터 모델
     * @return 노트 상세 페이지 뷰
     */
    @GetMapping("/{id}")
    public String getNoteDetailPage(@PathVariable("id") Long id, Model model) {

        model.addAttribute("detailNote", noteservice.getDetailNote(id));

        return "note/detail";
    }
    
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
     * @param redirectAttributes 리다이렉트 시 전달할 속성 //일단 보류
     * @return 노트 홈으로 리다이렉트
     */
    @PostMapping("/new")
    public String postAddNewNote(@ModelAttribute NoteDTO noteDTO, @RequestParam(value = "userId") Long userId) {
        
        noteservice.createNewNote(noteDTO, userId);

        return "redirect:/note/list?userId=" + userId;
    }
    
    /**
     * 노트 수정 페이지 조회
     * @param id 노트 ID
     * @param model 뷰에 전달할 데이터 모델
     * @return 노트 수정 페이지 뷰
     */
    @GetMapping("/update/{id}")
    public String getUpdateNotePage(@PathVariable("id") Long id, Model model) {

        model.addAttribute("note", noteservice.getDetailNote(id));

        return "note/update";
    }

    /**
     * 노트 수정 처리
     * @param id 노트 ID
     * @param noteDTO 수정할 노트 데이터
     * @param redirectAttributes 리다이렉트 시 전달할 속성 // 일단 보류
     * @return 노트 상세 페이지로 리다이렉트
     */
    @PutMapping("update/{id}")
    public String putUpdateNote(@PathVariable("id") Long id, @ModelAttribute NoteDTO noteDTO) {
        
        noteservice.updateNote(id, noteDTO);
        
        return "redirect:/note/" + id;
    }
    
    /**
     * 노트 삭제 처리
     * @param id 노트 ID
     * @param userId 사용자 ID
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 노트 홈으로 리다이렉트
     */
    @DeleteMapping("{id}")
    public String delelteNote(@PathVariable("id") Long id, @RequestParam(value = "userId") Long userId) {

        noteservice.deleteNote(id);

        return "redirect:/note/list?userId=" + userId;
    }
}