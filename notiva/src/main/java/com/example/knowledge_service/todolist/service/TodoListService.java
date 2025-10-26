package com.example.knowledge_service.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.knowledge_service.exception.AppException;
import com.example.knowledge_service.exception.ErrorCode;
import com.example.knowledge_service.todolist.domain.TodoList;
import com.example.knowledge_service.todolist.dto.TodoListDTO;
import com.example.knowledge_service.todolist.repository.TodoListRepository;
import com.example.knowledge_service.user.domain.User;
import com.example.knowledge_service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    /**
     * 특정 사용자의 모든 투두리스트 목록 조회
     * @param userId 사용자 ID
     * @return 투두리스트 목록
     */
    public List<TodoList> getTodoLists(Long userId) {

        return todoListRepository.findAllByUser_Id(userId);
    }

    /**
     * 투드리스트 생성 하는 함수
     * @param userId
     * @param todoListDTO
     * @throws USER_NOT_FOUND
     */
    public void createTodoList(Long userId, TodoListDTO todoListDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.USER_NOT_FOUND, "해당 사용자가 존재 하지 않습니다. userId = " + userId);
                });

        TodoList todo = TodoList.builder()
                .title(todoListDTO.getTitle())
                .complete(todoListDTO.getComplete())
                .startDay(todoListDTO.getStartDay())
                .endDay(todoListDTO.getEndDay())
                .user(user)
                .build();

        todoListRepository.save(todo);
    }

    /**
     * 수정하기 위한 투두리스트 디테일 조회
     * @param todoId
     * @throw TODOLIST_NOT_FOUND
     */
    public TodoList detailTodoList(Long todoId) {

        return todoListRepository.findById(todoId)
        .orElseThrow(() -> {
            return new AppException(ErrorCode.TODOLIST_NOT_FOUND, "해당 투두리스트가 존재 하지 않습니다. TodoId = " + todoId);
        });
    }

    /**
     * 투두리스트 업데이트 함수
     * @param todoId
     * @param todoListDTO
     */
    @Transactional
    public void updateTodo(Long todoId, TodoListDTO todoListDTO) {

        TodoList todoList = todoListRepository.findById(todoId)
            .orElseThrow(() -> {
                return new AppException(ErrorCode.TODOLIST_NOT_FOUND, "해당 투두리스트가 존재 하지 않습니다. Todo_Id = " + todoId);
            });

        if (todoListDTO.getTitle() != null) {
            todoList.setTitle(todoListDTO.getTitle());
        }

        if (todoListDTO.getComplete() != null) {
            todoList.setComplete(todoListDTO.getComplete());
        }

        if (todoListDTO.getStartDay() != null) {
            todoList.setStartDay(todoListDTO.getStartDay());
        }

        if (todoListDTO.getEndDay() != null) {
            todoList.setEndDay(todoListDTO.getEndDay());
        }
 
        todoListRepository.save(todoList);
    }

    /**
     * 투두리스트 삭제 함수
     * @param todoId
     */
    public void deleteTodo(Long todoId) {

        TodoList todoList = todoListRepository.findById(todoId)
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.TODOLIST_NOT_FOUND, "해당 투두리스트가 존재하지 않습니다. Todo_Id = " + todoId);
                });

        todoListRepository.delete(todoList);
    }
}
