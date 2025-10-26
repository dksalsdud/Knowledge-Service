package com.example.knowledge_service.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.knowledge_service.todolist.domain.TodoList;
import com.example.knowledge_service.todolist.repository.TodoListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    /**
     * 특정 사용자의 모든 노트 목록 조회
     * @param userId 사용자 ID
     * @return 투두리스트 목록
     */
    public List<TodoList> getTodoLists(Long userId) {

        return todoListRepository.findAllByUser_Id(userId);
    }
}
