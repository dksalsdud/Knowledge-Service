package com.example.knowledge_service.note.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.knowledge_service.note.service.NoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NotePageController {

    private final NoteService noteService;

    /**
     * 노트 리스트 페이지 조회
     * @param model 뷰에 전달할 데이터 모델
     * @param userId 사용자 ID
     * @return 노트 리스트 페이지 뷰
     */
    @GetMapping("/list")
    public String getNoteListPage(Model model, @RequestParam(value = "userId") Long userId) {

        model.addAttribute("listNotes", noteService.getListNotes(userId));
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

        model.addAttribute("detailNote", noteService.getDetailNote(id));

        return "note/detail";
    }
}
