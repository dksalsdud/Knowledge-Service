package com.example.knowledge_service.note.service;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.note.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {
    
    private final NoteRepository noteRepository;
}
