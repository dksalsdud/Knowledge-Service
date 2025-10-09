package com.example.knowledge_service.bookmark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.knowledge_service.bookmark.service.BookmarkService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/boomarks")
@RequiredArgsConstructor
public class BookmarkController {
    
    private final BookmarkService bookmarkService;
    
    /**
     * 북마크 목록 페이지 조회
     * 
     * @param userId 사용자 ID
     * @param model 뷰에 전달할 데이터 모델
     * @return 북마크 목록 페이지 뷰
     */
    @GetMapping
    public String getBookmarkListPage(@RequestParam Long userId, Model model) {
        
        model.addAttribute("bookmarks", bookmarkService.getUserBookmarks(userId));
        model.addAttribute("userId", userId);
        model.addAttribute("bookmarkCount", bookmarkService.getUserBookmarkCount(userId));
        
        return "bookmark/list";
    }
}