package com.example.knowledge_service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.knowledge_service.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
}
