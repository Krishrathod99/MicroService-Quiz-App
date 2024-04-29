package com.example.QuestionService.Controller;


import com.example.QuestionService.Entity.Question;
import com.example.QuestionService.Entity.Response;
import com.example.QuestionService.Exception.ApiResponse;
import com.example.QuestionService.Payload.QuestionDTO;
import com.example.QuestionService.Payload.QuestionWrapper;
import com.example.QuestionService.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

//    This is load balancing, It will check which instance of service is free according to that it will run on a particular instance
    @Autowired // Generally used for verifying on which port number particular instance of the service run
    private Environment environment;

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


//    Generate --> Quiz send http request to questionService for send it questions
    @GetMapping("/generate")  // Integer bcz we are going only questionId of the questions
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(
            @RequestParam String category, @RequestParam int totalQuestions){

        List<Integer> questions = this.questionService.getQuestionsForQuiz(category, totalQuestions);
        return new ResponseEntity<>(questions , HttpStatus.OK);
    }


//    GetQuestions(questionId) --> If QuizService send request for questions of their particular questionId, we don't want to send
                                // rightAns, category, difficultyLevel that's why QuestionWrapper as a return type
    @PostMapping("/getQuestions") // QuizService send questionId in requestBody which it wants
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionsIds){

        System.out.println(environment.getProperty("local.server.port"));  // It's print the port number on which particular QuestionsService Instance currently run

        List<QuestionWrapper> questionWrappers = this.questionService.getQuestionsFromId(questionsIds);
        return new ResponseEntity<>(questionWrappers , HttpStatus.OK);
    }

//    client play Quiz --> QuizService , QuizService ask QuestionService for Questions --> QuestionService
//    client got score from QuizService <-- QuizService got score from QuestionService <-- QuestionService calculate score & send to quizS

//    GetScore --> QuizService send the response given or submitted by client(play quiz)
    @PostMapping("/getScore")  // Here , QuizService provide questionId & response which was selected by the user
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses){
        Integer score = this.questionService.getScore(responses);
        return new ResponseEntity<>(score , HttpStatus.OK);
    }



}
