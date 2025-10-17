package com.example.knowledge_service.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.knowledge_service.bookmark.domain.Bookmark;
import com.example.knowledge_service.bookmark.dto.BookmarkDTO;
import com.example.knowledge_service.bookmark.repository.BookmarkRepository;
import com.example.knowledge_service.exception.AppException;
import com.example.knowledge_service.exception.ErrorCode;
import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.note.repository.NoteRepository;
import com.example.knowledge_service.user.domain.User;
import com.example.knowledge_service.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkService 테스트")
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private User testUser;
    private Note testNote;
    private Bookmark testBookmark;
    private Long userId;
    private Long noteId;
    private Long bookmarkId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        noteId = 100L;
        bookmarkId = 10L;

        // 테스트용 User 객체 생성
        testUser = User.builder()
                .id(userId)
                .email("test@example.com")
                .nickname("테스터")
                .build();

        // 테스트용 Note 객체 생성
        testNote = Note.builder()
                .id(noteId)
                .title("테스트 노트")
                .content("테스트 노트 내용입니다. 이것은 100자가 넘는 긴 내용을 테스트하기 위한 문장입니다. 더 많은 내용을 추가하여 preview 기능을 테스트합니다.")
                .user(testUser)
                .build();

        // 테스트용 Bookmark 객체 생성
        testBookmark = Bookmark.builder()
                .id(bookmarkId)
                .user(testUser)
                .note(testNote)
                .build();
    }

    @Test
    @DisplayName("사용자 북마크 목록 조회 - 성공")
    void getUserBookmarks_Success() {
        // Given
        List<Bookmark> bookmarks = Arrays.asList(testBookmark);
        when(bookmarkRepository.findAllByUserIdWithNote(userId)).thenReturn(bookmarks);

        // When
        List<BookmarkDTO> result = bookmarkService.getUserBookmarks(userId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookmarkId()).isEqualTo(bookmarkId);
        assertThat(result.get(0).getNoteId()).isEqualTo(noteId);
        assertThat(result.get(0).getNoteTitle()).isEqualTo("테스트 노트");
        assertThat(result.get(0).getAuthorName()).isEqualTo("테스터");
        verify(bookmarkRepository).findAllByUserIdWithNote(userId);
    }

    @Test
    @DisplayName("사용자 북마크 개수 조회 - 성공")
    void getUserBookmarkCount_Success() {
        // Given
        when(bookmarkRepository.countByUser_Id(userId)).thenReturn(5L);

        // When
        Long count = bookmarkService.getUserBookmarkCount(userId);

        // Then
        assertThat(count).isEqualTo(5L);
        verify(bookmarkRepository).countByUser_Id(userId);
    }

    @Test
    @DisplayName("북마크 추가 - 성공")
    void addBookmark_Success() {
        // Given
        when(bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(testNote));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(testBookmark);

        // When
        bookmarkService.addBookmark(userId, noteId);

        // Then
        verify(bookmarkRepository).existsByUser_IdAndNote_Id(userId, noteId);
        verify(userRepository).findById(userId);
        verify(noteRepository).findById(noteId);
        verify(bookmarkRepository).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 추가 - 이미 존재하는 경우 예외 발생")
    void addBookmark_AlreadyExists() {
        // Given
        when(bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> bookmarkService.addBookmark(userId, noteId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("이미 북마크에 추가되어 있습니다")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKMARK_ALREADY_EXISTS);

        verify(bookmarkRepository).existsByUser_IdAndNote_Id(userId, noteId);
        verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 추가 - 사용자를 찾을 수 없는 경우")
    void addBookmark_UserNotFound() {
        // Given
        when(bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookmarkService.addBookmark(userId, noteId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("북마크 추가 - 노트를 찾을 수 없는 경우")
    void addBookmark_NoteNotFound() {
        // Given
        when(bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookmarkService.addBookmark(userId, noteId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("노트를 찾을 수 없습니다")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOTE_NOT_FOUND);

        verify(noteRepository).findById(noteId);
    }

    @Test
    @DisplayName("북마크 삭제 (ID로) - 성공")
    void removeBookmark_Success() {
        // Given
        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(testBookmark));

        // When
        bookmarkService.removeBookmark(bookmarkId);

        // Then
        verify(bookmarkRepository).findById(bookmarkId);
        verify(bookmarkRepository).delete(testBookmark);
    }

    @Test
    @DisplayName("북마크 삭제 (ID로) - 북마크를 찾을 수 없는 경우")
    void removeBookmark_NotFound() {
        // Given
        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookmarkService.removeBookmark(bookmarkId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("북마크를 찾을 수 없습니다")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKMARK_NOT_FOUND);

        verify(bookmarkRepository).findById(bookmarkId);
    }

    @Test
    @DisplayName("북마크 삭제 (사용자 ID와 노트 ID로) - 성공")
    void removeBookmarkByUserAndNote_Success() {
        // Given
        when(bookmarkRepository.findByUser_IdAndNote_Id(userId, noteId))
                .thenReturn(Optional.of(testBookmark));

        // When
        bookmarkService.removeBookmarkByUserAndNote(userId, noteId);

        // Then
        verify(bookmarkRepository).findByUser_IdAndNote_Id(userId, noteId);
        verify(bookmarkRepository).delete(testBookmark);
    }

    @Test
    @DisplayName("북마크 삭제 (사용자 ID와 노트 ID로) - 북마크를 찾을 수 없는 경우")
    void removeBookmarkByUserAndNote_NotFound() {
        // Given
        when(bookmarkRepository.findByUser_IdAndNote_Id(userId, noteId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookmarkService.removeBookmarkByUserAndNote(userId, noteId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("북마크를 찾을 수 없습니다")
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKMARK_NOT_FOUND);

        verify(bookmarkRepository).findByUser_IdAndNote_Id(userId, noteId);
    }

    @Test
    @DisplayName("북마크 토글 - 추가")
    void toggleBookmark_Add() {
        // Given
        when(bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(testNote));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(testBookmark);

        // When
        boolean result = bookmarkService.toggleBookmark(userId, noteId);

        // Then
        assertThat(result).isTrue();
        verify(bookmarkRepository).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 토글 - 삭제")
    void toggleBookmark_Remove() {
        // Given
        when(bookmarkRepository.existsByUser_IdAndNote_Id(userId, noteId)).thenReturn(true);
        when(bookmarkRepository.findByUser_IdAndNote_Id(userId, noteId))
                .thenReturn(Optional.of(testBookmark));

        // When
        boolean result = bookmarkService.toggleBookmark(userId, noteId);

        // Then
        assertThat(result).isFalse();
        verify(bookmarkRepository).delete(testBookmark);
    }

    @Test
    @DisplayName("노트의 북마크 개수 조회 - 성공")
    void getNoteBookmarkCount_Success() {
        // Given
        when(bookmarkRepository.countByNote_Id(noteId)).thenReturn(10L);

        // When
        Long count = bookmarkService.getNoteBookmarkCount(noteId);

        // Then
        assertThat(count).isEqualTo(10L);
        verify(bookmarkRepository).countByNote_Id(noteId);
    }

    @Test
    @DisplayName("BookmarkDTO의 preview 길이 확인 - 100자 초과")
    void bookmarkDTO_PreviewLength_Over100() {
        // Given
        String longContent = "a".repeat(150);
        testNote = Note.builder()
                .id(noteId)
                .title("테스트")
                .content(longContent)
                .user(testUser)
                .build();
        
        testBookmark = Bookmark.builder()
                .id(bookmarkId)
                .user(testUser)
                .note(testNote)
                .build();

        // When
        BookmarkDTO dto = BookmarkDTO.from(testBookmark);

        // Then
        assertThat(dto.getNotePreview()).hasSize(103); // 100 + "..."
        assertThat(dto.getNotePreview()).endsWith("...");
    }

    @Test
    @DisplayName("BookmarkDTO의 preview 길이 확인 - 100자 이하")
    void bookmarkDTO_PreviewLength_Under100() {
        // Given
        String shortContent = "짧은 내용";
        testNote = Note.builder()
                .id(noteId)
                .title("테스트")
                .content(shortContent)
                .user(testUser)
                .build();
        
        testBookmark = Bookmark.builder()
                .id(bookmarkId)
                .user(testUser)
                .note(testNote)
                .build();

        // When
        BookmarkDTO dto = BookmarkDTO.from(testBookmark);

        // Thencom.example.knowledge_service.bookmark.repository.BookmarkRepository
        assertThat(dto.getNotePreview()).isEqualTo(shortContent);
        assertThat(dto.getNotePreview()).doesNotEndWith("...");
    }
}