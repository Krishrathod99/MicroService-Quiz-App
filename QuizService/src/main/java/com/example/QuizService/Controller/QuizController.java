package com.example.QuizService.Controller;


import com.example.QuizService.Entity.Response;
import com.example.QuizService.Exception.ApiResponse;
import com.example.QuizService.Payload.QuestionWrapper;
import com.example.QuizService.Payload.QuizDTO;
import com.example.QuizService.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;



//    When I call this method, I want to get the questions from QuestionService which is done by using eureka server, Now QuestionService
//    register itself in eureka server so this project(QuizService) can communicate with another Service(QuestionService)
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO){
        String string = this.quizService.createQuiz(quizDTO.getCategoryName(), quizDTO.getNumQuestions(), quizDTO.getTitle());
        return new ResponseEntity<>(string , HttpStatus.CREATED);
    }

    @GetMapping("/get/{quizId}") // QuestionWrapper cls instead Question class bcz I don't want User who is going to play the quiz that
                                //  get rightAnswer, difficultyLevel, category which is stored in Question class
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer quizId){
        List<QuestionWrapper> questionsForUser = this.quizService.getQuizQuestionsByQuizId(quizId);
        return new ResponseEntity<>(questionsForUser , HttpStatus.OK);
    }

    @PostMapping("/getScore/{quizId}")  // responses are actually answer given by the User, Generally requestBody is user send data
    public ResponseEntity<Integer> getScore(@PathVariable Integer quizId, @RequestBody List<Response> responses){
        int calculatedResult = this.quizService.calculateResult(quizId, responses);
        return new ResponseEntity<>(calculatedResult , HttpStatus.OK);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<ApiResponse> deleteQuiz(@PathVariable Integer quizId){
        this.quizService.deleteQuiz(quizId);
        return new ResponseEntity<>(new ApiResponse("Quiz deleted Successfully !!", true), HttpStatus.OK);
    }

}
