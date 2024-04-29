package com.example.QuizService.Feign;


import com.example.QuizService.Entity.Response;
import com.example.QuizService.Payload.QuestionWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("QUESTION-SERVICE")  // give the name of the service(client) which you want to connect
public interface QuizInterface {

//    Define the methods of QuestionService you want to access here (QuizService)
    @GetMapping("/api/question/generate")  // Integer bcz we are going only questionId of the questions
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam int totalQuestions);

    @PostMapping("/api/question/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionsId);

    @PostMapping("/api/question/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);

}
