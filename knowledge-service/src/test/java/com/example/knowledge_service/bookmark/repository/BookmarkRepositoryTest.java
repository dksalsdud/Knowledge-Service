package com.example.knowledge_service.bookmark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.knowledge_service.bookmark.domain.Bookmark;
import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.user.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("BookmarkRepository 테스트")
class BookmarkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private User testUser1;
    private User testUser2;
    private Note testNote1;
    private Note testNote2;
    private Bookmark bookmark1;
    private Bookmark bookmark2;

    @BeforeEach
    void setUp() {
        // 사용자 생성
        testUser1 = User.builder()
                .email("user1@example.com")
                .userPw("password123")
                .nickname("사용자1")
                .name("사용자1")
                .role("USER")
                .build();
        entityManager.persist(testUser1);

        testUser2 = User.builder()
                .email("user2@example.com")
                .userPw("password456")
                .nickname("사용자2")
                .name("사용자2")
                .role("USER")
                .build();
        entityManager.persist(testUser2);

        // 노트 생성
        testNote1 = Note.builder()
                .title("Spring Boot 학습")
                .content("Spring Boot에 대한 학습 내용입니다.")
                .user(testUser1)
                .build();
        entityManager.persist(testNote1);

        testNote2 = Note.builder()
                .title("JPA 완벽 가이드")
                .content("JPA에 대한 완벽한 가이드입니다.")
                .user(testUser2)
                .build();
        entityManager.persist(testNote2);

        // 북마크 생성
        bookmark1 = Bookmark.builder()
                .user(testUser1)
                .note(testNote1)
                .build();
        entityManager.persist(bookmark1);

        bookmark2 = Bookmark.builder()
                .user(testUser1)
                .note(testNote2)
                .build();
        entityManager.persist(bookmark2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("특정 사용자의 북마크 목록 조회 (Fetch Join) - 성공")
    void findAllByUserIdWithNote_Success() {
        // When
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdWithNote(testUser1.getId());

        // Then
        assertThat(bookmarks).hasSize(2);
        assertThat(bookmarks.get(0).getCreatedAt())
                .isAfter(bookmarks.get(1).getCreatedAt()); // 최신순 정렬 확인
        
        // N+1 문제 방지 확인 (Fetch Join)
        assertThat(bookmarks.get(0).getNote()).isNotNull();
        assertThat(bookmarks.get(0).getNote().getUser()).isNotNull();
    }

    @Test
    @DisplayName("북마크가 없는 사용자의 목록 조회 - 빈 리스트 반환")
    void findAllByUserIdWithNote_EmptyList() {
        // When
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdWithNote(testUser2.getId());

        // Then
        assertThat(bookmarks).isEmpty();
    }

    @Test
    @DisplayName("특정 사용자의 북마크 개수 조회 - 성공")
    void countByUser_Id_Success() {
        // When
        Long count = bookmarkRepository.countByUser_Id(testUser1.getId());

        // Then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("북마크가 없는 사용자의 개수 조회 - 0 반환")
    void countByUser_Id_Zero() {
        // When
        Long count = bookmarkRepository.countByUser_Id(testUser2.getId());

        // Then
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("특정 사용자가 특정 노트를 북마크했는지 확인 - 존재")
    void existsByUser_IdAndNote_Id_True() {
        // When
        boolean exists = bookmarkRepository.existsByUser_IdAndNote_Id(
                testUser1.getId(), 
                testNote1.getId()
        );

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("특정 사용자가 특정 노트를 북마크했는지 확인 - 미존재")
    void existsByUser_IdAndNote_Id_False() {
        // When
        boolean exists = bookmarkRepository.existsByUser_IdAndNote_Id(
                testUser2.getId(), 
                testNote1.getId()
        );

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("특정 사용자의 특정 노트 북마크 조회 - 성공")
    void findByUser_IdAndNote_Id_Success() {
        // When
        Optional<Bookmark> bookmark = bookmarkRepository.findByUser_IdAndNote_Id(
                testUser1.getId(), 
                testNote1.getId()
        );

        // Then
        assertThat(bookmark).isPresent();
        assertThat(bookmark.get().getUser().getId()).isEqualTo(testUser1.getId());
        assertThat(bookmark.get().getNote().getId()).isEqualTo(testNote1.getId());
    }

    @Test
    @DisplayName("특정 사용자의 특정 노트 북마크 조회 - 미존재")
    void findByUser_IdAndNote_Id_NotFound() {
        // When
        Optional<Bookmark> bookmark = bookmarkRepository.findByUser_IdAndNote_Id(
                testUser2.getId(), 
                testNote1.getId()
        );

        // Then
        assertThat(bookmark).isEmpty();
    }

    @Test
    @DisplayName("특정 노트의 북마크 개수 조회 - 성공")
    void countByNote_Id_Success() {
        // Given - testNote1에 추가 북마크 생성
        Bookmark additionalBookmark = Bookmark.builder()
                .user(testUser2)
                .note(testNote1)
                .build();
        entityManager.persist(additionalBookmark);
        entityManager.flush();

        // When
        Long count = bookmarkRepository.countByNote_Id(testNote1.getId());

        // Then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("북마크가 없는 노트의 개수 조회 - 0 반환")
    void countByNote_Id_Zero() {
        // Given - 새로운 노트 생성 (북마크 없음)
        Note newNote = Note.builder()
                .title("새 노트")
                .content("내용")
                .user(testUser1)
                .build();
        entityManager.persist(newNote);
        entityManager.flush();

        // When
        Long count = bookmarkRepository.countByNote_Id(newNote.getId());

        // Then
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("북마크 생성 시 createdAt 자동 설정 확인")
    void bookmark_CreatedAt_AutoSet() {
        // Given
        Bookmark newBookmark = Bookmark.builder()
                .user(testUser2)
                .note(testNote1)
                .build();

        // When
        Bookmark savedBookmark = bookmarkRepository.save(newBookmark);
        entityManager.flush();

        // Then
        assertThat(savedBookmark.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("북마크 삭제 - 성공")
    void deleteBookmark_Success() {
        // Given
        Long bookmarkId = bookmark1.getId();

        // When
        bookmarkRepository.delete(bookmark1);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Bookmark> deletedBookmark = bookmarkRepository.findById(bookmarkId);
        assertThat(deletedBookmark).isEmpty();
    }

    @Test
    @DisplayName("사용자 삭제 시 북마크도 함께 삭제되는지 확인 (Cascade 테스트)")
    void deleteUser_CascadeBookmarks() {
        // Given
        Long userId = testUser1.getId();
        Long bookmarkCount = bookmarkRepository.countByUser_Id(userId);
        assertThat(bookmarkCount).isGreaterThan(0L);

        // When
        entityManager.remove(entityManager.find(User.class, userId));
        entityManager.flush();
        entityManager.clear();

        // Then
        Long remainingBookmarks = bookmarkRepository.countByUser_Id(userId);
        assertThat(remainingBookmarks).isEqualTo(0L);
    }

    @Test
    @DisplayName("노트 삭제 시 북마크도 함께 삭제되는지 확인 (Cascade 테스트)")
    void deleteNote_CascadeBookmarks() {
        // Given
        Long noteId = testNote1.getId();
        Long bookmarkCount = bookmarkRepository.countByNote_Id(noteId);
        assertThat(bookmarkCount).isGreaterThan(0L);

        // When
        entityManager.remove(entityManager.find(Note.class, noteId));
        entityManager.flush();
        entityManager.clear();

        // Then
        Long remainingBookmarks = bookmarkRepository.countByNote_Id(noteId);
        assertThat(remainingBookmarks).isEqualTo(0L);
    }
}