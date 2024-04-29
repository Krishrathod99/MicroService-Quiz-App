package com.example.Quiz.Application.Controller;

import com.example.Quiz.Application.Entity.Question;
import com.example.Quiz.Application.Exception.ApiResponse;
import com.example.Quiz.Application.Payload.QuestionDTO;
import com.example.Quiz.Application.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO){
        QuestionDTO savedQuestionDTO = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(savedQuestionDTO , HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(@RequestBody QuestionDTO questionDTO ,@PathVariable("questionId") Integer questionId){
        QuestionDTO updatedQuestion = questionService.updateQuestion(questionDTO, questionId);
        return new ResponseEntity<>(updatedQuestion , HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiResponse> deleteQuestion(@PathVariable("questionId") Integer questionId){
        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/allQuestions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(){
        try {
            List<QuestionDTO> allQuestions = questionService.getAllQuestions();
            return new ResponseEntity<>(allQuestions, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>() , HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/category/{category}")
    public List<Question> getQuestionsByCategory(@PathVariable("category") String category){
        return questionService.getQuestionsByCategory(category);
    }

}
