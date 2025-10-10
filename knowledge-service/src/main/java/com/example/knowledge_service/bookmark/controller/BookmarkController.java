package com.example.knowledge_service.bookmark.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/list")
    public String getBookmarkListPage(@RequestParam Long userId, Model model) {
        
        model.addAttribute("bookmarks", bookmarkService.getUserBookmarks(userId));
        model.addAttribute("userId", userId);
        model.addAttribute("bookmarkCount", bookmarkService.getUserBookmarkCount(userId));
        
        return "bookmark/list";
    }

    /**
     * 북마크 추가 처리
     * 
     * @param userId 사용자 ID
     * @param noteId 노트 ID
     * @param redirectAttributes 리다이렉트 시 전달할 속성 // 추후 고려
     * @return 이전 페이지로 리다이렉트
     */
    @PostMapping("/add")
    public String addBookmark(@RequestParam Long userId,
                             @RequestParam Long noteId,
                             RedirectAttributes redirectAttributes) {
        
        bookmarkService.addBookmark(userId, noteId);

        redirectAttributes.addFlashAttribute("successMessage", "북마크가 성공적으로 추가되었습니다.");
        
        return "redirect:/notes/" + noteId; // 성공 시 상세 페이지 등으로 이동
    }

     /**
     * 북마크 삭제 처리 (북마크 ID로)
     * 
     * @param bookmarkId 북마크 ID
     * @param userId 사용자 ID
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 북마크 목록으로 리다이렉트
     */
    @DeleteMapping("/remove/{bookmarkId}")
    public String removeBookmark(@PathVariable Long bookmarkId,
                                @RequestParam Long userId,
                                RedirectAttributes redirectAttributes) {
        
        bookmarkService.removeBookmark(bookmarkId);
        
        redirectAttributes.addFlashAttribute("message", "북마크가 삭제되었습니다.");
        
        return "redirect:/bookmarks/list?userId=" + userId;
    }
}