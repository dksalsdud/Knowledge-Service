package com.example.knowledge_service.notetag.service;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.notetag.repository.NoteTagRepository;

import lombok.RequiredArgsConstructor;

/**
 * NoteTagService
 * 정의 : 노트와 태그를 관리하는 서비스 로직
 * 태그로 특정 노트 조회 및 노트 조회 시 모든 태그 조회 등
 * 태그 노트 CRUD 등
 * 컨트롤러나 서비스단에서 의존성 주입 후 사용할 수 있도록 개발하는 것이 목표
 */
@Service
@RequiredArgsConstructor
public class NoteTagService {
    
    private final NoteTagRepository noteTagRepository;
}
