package com.example.Quiz.Application.Controller;

import com.example.Quiz.Application.Entity.Response;
import com.example.Quiz.Application.Exception.ApiResponse;
import com.example.Quiz.Application.Payload.QuestionWrapper;
import com.example.Quiz.Application.Payload.QuizDTO;
import com.example.Quiz.Application.Service.QuizService;
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

    @PostMapping("/create")
    public ResponseEntity<QuizDTO> createQuiz(
            @RequestParam String category, @RequestParam int numQ, @RequestParam String title){

        QuizDTO quizDTO = this.quizService.createQuiz(title, numQ, category);
        return new ResponseEntity<>(quizDTO , HttpStatus.CREATED);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<ApiResponse> deleteQuiz(@PathVariable Integer quizId){
        this.quizService.deleteQuiz(quizId);
        return new ResponseEntity<>(new ApiResponse("Quiz deleted Successfully !!", true), HttpStatus.OK);
    }

    @GetMapping("/get/{quizId}") // QuestionWrapper cls instead Question class bcz I don't want User who is going to play the quiz that
                                //  get rightAnswer, difficultyLevel, category which is stored in Question class

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer quizId){
        List<QuestionWrapper> questionsForUser = this.quizService.getQuizQuestionsByQuizId(quizId);
        return new ResponseEntity<>(questionsForUser , HttpStatus.OK);
    }

    @PostMapping("/submit/{quizId}")  // responses are actually answer given by the User, Generally requestBody is user send data
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer quizId, @RequestBody List<Response> responses){
        int calculatedResult = this.quizService.calculateResult(quizId, responses);
        return new ResponseEntity<>(calculatedResult , HttpStatus.OK);
    }

}
