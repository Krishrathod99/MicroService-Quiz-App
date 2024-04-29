package com.example.QuizService.Service;

import com.example.QuizService.Entity.Quiz;
import com.example.QuizService.Entity.Response;
import com.example.QuizService.Exception.ResourceNotFoundException;
import com.example.QuizService.Feign.QuizInterface;
import com.example.QuizService.Payload.QuestionWrapper;
import com.example.QuizService.Repository.QuizRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QuizInterface quizInterface;


//    QuizService wants questions from QuestionsService and QuestionService give it
    public String createQuiz(String category, int numQ, String title) {

//        Here, quizService project need to call QuestionService project's method which is getQuestionsForQuiz()
//        To send request to the QuestionService RestTemplate is used, but for that we need to give IP add.,port number so the shortcut is
//               to use feign, instead of handling all stuff by own, feign says you just tell me which service you want to connect, I will do it for you

//        List<Integer> questions =  // call the generate url - RestTemplate --> http://localhost:8080/api/question/generate

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();// getBody() bcz it returns responseEntity<>()

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        this.quizRepository.save(quiz);

        return "Success";
    }

    public List<QuestionWrapper> getQuizQuestionsByQuizId(Integer quizId) {

//        Now Here, QuizService has only questionsIds, but it wants whole question so for that QuizService need to interact with QuestionService
        Quiz quiz = this.quizRepository.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "Quiz Id", quizId));
        List<Integer> questionIds = quiz.getQuestionIds();

//        Here, we call getQuestionsFromId method from questionService
        List<QuestionWrapper> questions = quizInterface.getQuestionsFromId(questionIds).getBody();
        return questions;
    }


    public int calculateResult(Integer quizId, List<Response> responses) {

//      Now Here, QuizService wants score of the quiz and QuestionService is responsible for give score
        Quiz quiz = this.quizRepository.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "Quiz Id", quizId));

        return quizInterface.getScore(responses).getBody();
    }

    public void deleteQuiz(Integer quizId) {
        this.quizRepository.deleteById(quizId);
    }

}
