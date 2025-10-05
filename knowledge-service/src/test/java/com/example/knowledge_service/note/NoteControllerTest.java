package com.example.knowledge_service.note;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.example.knowledge_service.note.controller.NoteController;
import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.note.dto.NoteDTO;
import com.example.knowledge_service.note.service.NoteService;
import com.example.knowledge_service.user.domain.User;

/**
 * NoteController 웹 레이어 테스트
 */
@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("NoteController 테스트")
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    private User testUser;
    private Note testNote;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("testuser")
                .nickname("testuser")
                .email("test@example.com")
                .build();

        testNote = Note.builder()
                .id(1L)
                .title("테스트 노트")
                .content("테스트 내용")
                .user(testUser)
                .build();
    }

    @Test
    @DisplayName("노트 홈 페이지 조회")
    void getNoteHomePage() throws Exception {
        // given
        List<Note> noteList = Arrays.asList(testNote);
        when(noteService.getListNotes(1L)).thenReturn(noteList);

        // when & then
        mockMvc.perform(get("/notes")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/home"))
                .andExpect(model().attributeExists("listNotes"))
                .andExpect(model().attributeExists("userId"));

        verify(noteService, times(1)).getListNotes(1L);
    }

    @Test
    @DisplayName("노트 리스트 페이지 조회")
    void getNoteListPage() throws Exception {
        // given
        List<Note> noteList = Arrays.asList(testNote);
        when(noteService.getListNotes(1L)).thenReturn(noteList);

        // when & then
        mockMvc.perform(get("/notes/list")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/list"))
                .andExpect(model().attributeExists("listNotes"));

        verify(noteService, times(1)).getListNotes(1L);
    }

    @Test
    @DisplayName("노트 상세 페이지 조회")
    void getNoteDetailPage() throws Exception {
        // given
        when(noteService.getDetailNote(1L)).thenReturn(testNote);

        // when & then
        mockMvc.perform(get("/notes/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/detail"))
                .andExpect(model().attributeExists("detailNote"));

        verify(noteService, times(1)).getDetailNote(1L);
    }

    @Test
    @DisplayName("노트 작성 페이지 조회")
    void getNewNotePage() throws Exception {
        // when & then
        mockMvc.perform(get("/notes/new")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/write"))
                .andExpect(model().attributeExists("newNote"))
                .andExpect(model().attributeExists("userId"));
    }

    @Test
    @DisplayName("노트 생성 처리")
    void postAddNewNote() throws Exception {
        // given
        doNothing().when(noteService).createNewNote(any(NoteDTO.class), eq(1L));

        // when & then
        mockMvc.perform(post("/notes/new")
                        .param("userId", "1")
                        .param("title", "새 노트")
                        .param("content", "새 노트 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list?userId=1"));

        verify(noteService, times(1)).createNewNote(any(NoteDTO.class), eq(1L));
    }

    @Test
    @DisplayName("노트 수정 페이지 조회")
    void getUpdateNotePage() throws Exception {
        // given
        when(noteService.getDetailNote(1L)).thenReturn(testNote);

        // when & then
        mockMvc.perform(get("/notes/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/update"))
                .andExpect(model().attributeExists("note"));

        verify(noteService, times(1)).getDetailNote(1L);
    }

    @Test
    @DisplayName("노트 수정 처리")
    void putUpdateNote() throws Exception {
        // given
        doNothing().when(noteService).updateNote(eq(1L), any(NoteDTO.class));

        // when & then
        mockMvc.perform(put("/notes/update/1")
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/1"));

        verify(noteService, times(1)).updateNote(eq(1L), any(NoteDTO.class));
    }

    @Test
    @DisplayName("노트 삭제 처리")
    void deleteNote() throws Exception {
        // given
        doNothing().when(noteService).deleteNote(1L);

        // when & then
        mockMvc.perform(delete("/notes/1")
                        .param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list?userId=1"));

        verify(noteService, times(1)).deleteNote(1L);
    }
}