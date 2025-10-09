package com.example.knowledge_service.bookmark.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.knowledge_service.bookmark.domain.Bookmark;
import com.example.knowledge_service.bookmark.dto.BookmarkDTO;
import com.example.knowledge_service.bookmark.repository.BookmarkRepository;
import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

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

    /**
     * 북마크 추가
     * 이미 북마크가 존재하면 예외 발생
     * 
     * @param userId 사용자 ID
     * @param noteId 노트 ID
     * @return 생성된 북마크 DTO
     * @throws BookmarkAlreadyExistsException 이미 북마크가 존재하는 경우
     */
    @Transactional
    public BookmarkDTO addBookmark(Long userId, Long noteId) {
        
        // 중복 체크
        if (bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)) {

            throw new BookmarkAlreadyExistsException(userId, noteId);
        }
        
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new RuntimeException("사용자를 찾을 수 없습니다.");
                });
        
        // 노트 조회
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> {
                    return new RuntimeException("노트를 찾을 수 없습니다.");
                });
        
        // 북마크 생성 및 저장
        Bookmark bookmark = Bookmark.create(user, note);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        
        
        return BookmarkDTO.from(savedBookmark);
    }
}