package com.example.Quiz.Application.Repository;

import com.example.Quiz.Application.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question , Integer> {

    List<Question> findByCategory(String category);

//    query for get random numQ(5) questions of quiz
    @Query(value = "SELECT * FROM question q WHERE q.category = :cat ORDER BY RAND() LIMIT :numQ" , nativeQuery = true)
    List<Question> getRandomQuestionsByCategory(@Param("numQ") int numQ, @Param("cat") String cat);

}
