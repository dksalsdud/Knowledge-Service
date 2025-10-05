package com.example.knowledge_service.note;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.note.dto.NoteDTO;
import com.example.knowledge_service.note.repository.NoteRepository;
import com.example.knowledge_service.note.service.NoteService;
import com.example.knowledge_service.user.domain.User;
import com.example.knowledge_service.user.repository.UserRepository;

/**
 * NoteService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NoteService 테스트")
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    private User testUser;
    private Note testNote;
    private NoteDTO testNoteDTO;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 객체 생성
        testUser = User.builder()
                .id(1L)
                .name("testuser")
                .nickname("testuser")
                .email("test@example.com")
                .build();

        // 테스트용 노트 객체 생성
        testNote = Note.builder()
                .id(1L)
                .title("테스트 노트")
                .content("테스트 내용입니다.")
                .user(testUser)
                .build();

        // 테스트용 DTO 생성
        testNoteDTO = new NoteDTO();
        testNoteDTO.setTitle("새 노트 제목");
        testNoteDTO.setContent("새 노트 내용");
    }

    @Test
    @DisplayName("사용자의 노트 목록 조회 성공")
    void getListNotes_Success() {
        // given
        List<Note> noteList = Arrays.asList(testNote);
        when(noteRepository.findAllByUser_IdOrderByUpdatedAtDesc(1L)).thenReturn(noteList);

        // when
        List<Note> result = noteService.getListNotes(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("테스트 노트");
        verify(noteRepository, times(1)).findAllByUser_IdOrderByUpdatedAtDesc(1L);
    }

    @Test
    @DisplayName("노트 상세 조회 성공")
    void getDetailNote_Success() {
        // given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        // when
        Note result = noteService.getDetailNote(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("테스트 노트");
        assertThat(result.getContent()).isEqualTo("테스트 내용입니다.");
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("노트 상세 조회 실패 - 존재하지 않는 노트")
    void getDetailNote_NotFound() {
        // given
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> noteService.getDetailNote(999L))
                .isInstanceOf(RuntimeException.class);
        verify(noteRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("새 노트 생성 성공")
    void createNewNote_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // when
        noteService.createNewNote(testNoteDTO, 1L);

        // then
        verify(userRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("새 노트 생성 실패 - 존재하지 않는 사용자")
    void createNewNote_UserNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> noteService.createNewNote(testNoteDTO, 999L))
                .isInstanceOf(RuntimeException.class);
        verify(userRepository, times(1)).findById(999L);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("노트 수정 성공")
    void updateNote_Success() {
        // given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        NoteDTO updateDTO = new NoteDTO();
        updateDTO.setTitle("수정된 제목");
        updateDTO.setContent("수정된 내용");

        // when
        noteService.updateNote(1L, updateDTO);

        // then
        assertThat(testNote.getTitle()).isEqualTo("수정된 제목");
        assertThat(testNote.getContent()).isEqualTo("수정된 내용");
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(testNote);
    }

    @Test
    @DisplayName("노트 수정 실패 - 존재하지 않는 노트")
    void updateNote_NotFound() {
        // given
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> noteService.updateNote(999L, testNoteDTO))
                .isInstanceOf(RuntimeException.class);
        verify(noteRepository, times(1)).findById(999L);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("노트 삭제 성공")
    void deleteNote_Success() {
        // given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        doNothing().when(noteRepository).delete(testNote);

        // when
        noteService.deleteNote(1L);

        // then
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).delete(testNote);
    }

    @Test
    @DisplayName("노트 삭제 실패 - 존재하지 않는 노트")
    void deleteNote_NotFound() {
        // given
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> noteService.deleteNote(999L))
                .isInstanceOf(RuntimeException.class);
        verify(noteRepository, times(1)).findById(999L);
        verify(noteRepository, never()).delete(any(Note.class));
    }
}