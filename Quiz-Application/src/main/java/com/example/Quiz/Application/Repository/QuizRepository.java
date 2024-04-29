package com.example.Quiz.Application.Repository;

import com.example.Quiz.Application.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
