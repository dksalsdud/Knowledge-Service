package com.example.knowledge_service.note.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.exception.AppException;
import com.example.knowledge_service.exception.ErrorCode;
import com.example.knowledge_service.note.domain.Note;
import com.example.knowledge_service.note.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {
    
    private final NoteRepository noteRepository;

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
     * @param id 노트 ID
     * @return 노트 엔티티
     * @throws RuntimeException 노트를 찾을 수 없을 경우
     */
    public Note getDetailNote(Long id) {

        return noteRepository.findById(id)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.NOTE_NOT_FOUND, id + " 를 찾을 수 없습니다.");
                });
    }
}
