package com.example.knowledge_service.bookmark.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.knowledge_service.bookmark.dto.BookmarkDTO;
import com.example.knowledge_service.bookmark.service.BookmarkService;

@WebMvcTest(BookmarkController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("BookmarkController 테스트")
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookmarkService bookmarkService;

    private List<BookmarkDTO> mockBookmarks;
    private Long testUserId;
    private Long testNoteId;
    private Long testBookmarkId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testNoteId = 100L;
        testBookmarkId = 10L;

        // Mock 북마크 데이터 설정
        BookmarkDTO bookmark1 = BookmarkDTO.builder()
                .bookmarkId(1L)
                .noteId(100L)
                .userId(testUserId)
                .noteTitle("Spring Boot 학습 노트")
                .notePreview("Spring Boot는 스프링 프레임워크 기반의...")
                .authorName("홍길동")
                .noteCreatedAt(LocalDateTime.now().minusDays(5))
                .bookmarkedAt(LocalDateTime.now().minusDays(2))
                .build();

        BookmarkDTO bookmark2 = BookmarkDTO.builder()
                .bookmarkId(2L)
                .noteId(101L)
                .userId(testUserId)
                .noteTitle("JPA 완벽 가이드")
                .notePreview("JPA는 Java Persistence API의 약자로...")
                .authorName("김철수")
                .noteCreatedAt(LocalDateTime.now().minusDays(10))
                .bookmarkedAt(LocalDateTime.now().minusDays(1))
                .build();

        mockBookmarks = Arrays.asList(bookmark1, bookmark2);
    }

    @Test
    @DisplayName("북마크 목록 페이지 조회 - 성공")
    void getBookmarkListPage_Success() throws Exception {
        // Given
        when(bookmarkService.getUserBookmarks(testUserId)).thenReturn(mockBookmarks);
        when(bookmarkService.getUserBookmarkCount(testUserId)).thenReturn(2L);

        // When & Then
        mockMvc.perform(get("/bookmarks/list")
                        .param("userId", String.valueOf(testUserId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bookmark/list"))
                .andExpect(model().attributeExists("bookmarks"))
                .andExpect(model().attribute("userId", testUserId))
                .andExpect(model().attribute("bookmarkCount", 2L));

        verify(bookmarkService).getUserBookmarks(testUserId);
        verify(bookmarkService).getUserBookmarkCount(testUserId);
    }

    @Test
    @DisplayName("북마크 목록 페이지 조회 - 빈 목록")
    void getBookmarkListPage_EmptyList() throws Exception {
        // Given
        when(bookmarkService.getUserBookmarks(testUserId)).thenReturn(Arrays.asList());
        when(bookmarkService.getUserBookmarkCount(testUserId)).thenReturn(0L);

        // When & Then
        mockMvc.perform(get("/bookmarks/list")
                        .param("userId", String.valueOf(testUserId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bookmark/list"))
                .andExpect(model().attribute("bookmarkCount", 0L));
    }

    @Test
    @DisplayName("북마크 추가 - 성공")
    void addBookmark_Success() throws Exception {
        // Given
        doNothing().when(bookmarkService).addBookmark(testUserId, testNoteId);

        // When & Then
        mockMvc.perform(post("/bookmarks/add")
                        .param("userId", String.valueOf(testUserId))
                        .param("noteId", String.valueOf(testNoteId)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes/" + testNoteId))
                .andExpect(flash().attribute("successMessage", "북마크가 성공적으로 추가되었습니다."));

        verify(bookmarkService).addBookmark(testUserId, testNoteId);
    }

    @Test
    @DisplayName("북마크 삭제 (북마크 ID) - 성공")
    void removeBookmark_Success() throws Exception {
        // Given
        doNothing().when(bookmarkService).removeBookmark(testBookmarkId);

        // When & Then
        mockMvc.perform(delete("/bookmarks/remove/{bookmarkId}", testBookmarkId)
                        .param("userId", String.valueOf(testUserId)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookmarks/list?userId=" + testUserId))
                .andExpect(flash().attribute("message", "북마크가 삭제되었습니다."));

        verify(bookmarkService).removeBookmark(testBookmarkId);
    }

    @Test
    @DisplayName("북마크 삭제 (사용자 ID와 노트 ID) - 성공")
    void removeBookmarkByNote_Success() throws Exception {
        // Given
        doNothing().when(bookmarkService).removeBookmarkByUserAndNote(testUserId, testNoteId);

        // When & Then
        mockMvc.perform(delete("/bookmarks/remove")
                        .param("userId", String.valueOf(testUserId))
                        .param("noteId", String.valueOf(testNoteId)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes/" + testNoteId))
                .andExpect(flash().attribute("message", "북마크가 삭제되었습니다."));

        verify(bookmarkService).removeBookmarkByUserAndNote(testUserId, testNoteId);
    }

    @Test
    @DisplayName("북마크 토글 - 추가")
    void toggleBookmark_Add() throws Exception {
        // Given
        when(bookmarkService.toggleBookmark(testUserId, testNoteId)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/bookmarks/toggle")
                        .param("userId", String.valueOf(testUserId))
                        .param("noteId", String.valueOf(testNoteId)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes/" + testNoteId))
                .andExpect(flash().attribute("message", "북마크에 추가되었습니다."));

        verify(bookmarkService).toggleBookmark(testUserId, testNoteId);
    }

    @Test
    @DisplayName("북마크 토글 - 삭제")
    void toggleBookmark_Remove() throws Exception {
        // Given
        when(bookmarkService.toggleBookmark(testUserId, testNoteId)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/bookmarks/toggle")
                        .param("userId", String.valueOf(testUserId))
                        .param("noteId", String.valueOf(testNoteId)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes/" + testNoteId))
                .andExpect(flash().attribute("message", "북마크가 해제되었습니다."));

        verify(bookmarkService).toggleBookmark(testUserId, testNoteId);
    }
}