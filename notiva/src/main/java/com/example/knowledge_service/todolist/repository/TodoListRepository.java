package com.example.knowledge_service.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.knowledge_service.todolist.domain.TodoList;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    
}
