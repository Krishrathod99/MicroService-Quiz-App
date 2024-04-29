package com.example.QuestionService.Repository;

import com.example.QuestionService.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByCategory(String category);

//    query for get random numQ(5) questions of quiz
//    @Query(value = "SELECT q.questionId FROM question AS q WHERE q.category = :cat ORDER BY RAND() LIMIT :numQ" , nativeQuery = true)
//    List<Integer> getRandomQuestionsByCategory(@Param("numQ") int numQ, @Param("cat") String cat);

    @Query(value = "SELECT q.question_id FROM question AS q WHERE q.category = :cat ORDER BY RAND() LIMIT :numQ", nativeQuery = true)
    List<Integer> getRandomQuestionsByCategory(@Param("cat") String category, @Param("numQ") int limit);


}
