package com.example.knowledge_service.bookmark.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.bookmark.domain.Bookmark;
import com.example.knowledge_service.bookmark.dto.BookmarkDTO;
import com.example.knowledge_service.bookmark.repository.BookmarkRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    /**
     * 특정 사용자의 모든 북마크 목록 조회
     * 
     * @param userId 사용자 ID
     * @return 북마크 DTO 목록 (최신순)
     */
    public List<BookmarkDTO> getUserBookmarks(Long userId) {

        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdWithNote(userId);

        return bookmarks.stream()
                .map(BookmarkDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자의 북마크 개수 조회
     * 
     * @param userId 사용자 ID
     * @return 북마크 개수
     */
    public Long getUserBookmarkCount(Long userId) {

        return bookmarkRepository.countByUser_Id(userId);
    }
}