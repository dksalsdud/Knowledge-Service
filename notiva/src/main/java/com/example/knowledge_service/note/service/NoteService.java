package com.example.knowledge_service.note.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.knowledge_service.exception.AppException;
import com.example.knowledge_service.exception.ErrorCode;
import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.note.dto.NoteDTO;
import com.example.knowledge_service.note.repository.NoteRepository;
import com.example.knowledge_service.user.domain.User;
import com.example.knowledge_service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {
    
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    /**
     * 특정 사용자의 모든 노트 목록 조회
     * @param userId 사용자 ID
     * @return 노트 목록
     */
    public List<Note> getListNotes(Long userId) {

        return noteRepository.findAllByUser_IdOrderByUpdatedAtDesc(userId);
    }

    /**
     * 노트 상세 정보 조회
     * @param noteId 노트 ID
     * @return 노트 엔티티
     * @throws NOTE_NOT_FOUND 노트를 찾을 수 없을 경우
     */
    public Note getDetailNote(Long noteId) {

        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> {
                return new AppException(ErrorCode.NOTE_NOT_FOUND, "해당 노트를 찾을 수 없습니다. Note_Id = " + noteId);
            });
        
        // 방문자 수 증가
        note.setViewCount(note.getViewCount() + 1);

        // 업데이트된 노트를 저장
        noteRepository.save(note);

        return note;
    }

    /**
     * 새로운 노트 생성
     * @param noteDTO 노트 생성 데이터
     * @param userId 사용자 ID
     * @throws USER_NOT_FOUND 사용자를 찾을 수 없을 경우
     */
    @Transactional
    public void createNewNote(NoteDTO noteDTO, Long userId) {

       User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다. User_Id = " + userId);
                });

        Note note = Note.builder()
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .visibility(noteDTO.getVisibility())
                .user(user)
                .build();

        noteRepository.save(note);
    }

    /**
     * 노트 수정
     * @param noteId 노트 ID
     * @param noteDTO 수정할 노트 데이터
     * @throws NOTE_NOT_FOUND 노트를 찾을 수 없을 경우
     */
    @Transactional
	public void updateNote(Long noteId, NoteDTO noteDTO) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.NOTE_NOT_FOUND, "해당 노트가 존재하지 않습니다. Note_Id = " + noteId);
                });

        if (noteDTO.getTitle() != null) {
            note.setTitle(noteDTO.getTitle());   
        }

        if (noteDTO.getContent() != null) {
            note.setContent(noteDTO.getContent());
        }          
        // updatedAt은 @PreUpdate에서 자동으로 업데이트됨

        noteRepository.save(note);
	}

    /**
     * 노트 삭제
     * @param noteId 노트 ID
     * @throws NOTE_NOT_FOUND 노트를 찾을 수 없을 경우
     */
    @Transactional
	public void deleteNote(Long noteId) {

		Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.NOTE_NOT_FOUND, "해당 노트가 존재하지 않습니다. Note_Id = " + noteId);
                });

        noteRepository.delete(note);
	}
}
