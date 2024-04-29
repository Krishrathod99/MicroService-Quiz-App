package com.example.Quiz.Application.Service;

import com.example.Quiz.Application.Entity.Question;
import com.example.Quiz.Application.Entity.Quiz;
import com.example.Quiz.Application.Entity.Response;
import com.example.Quiz.Application.Exception.ApiResponse;
import com.example.Quiz.Application.Exception.ResourceNotFoundException;
import com.example.Quiz.Application.Payload.QuestionWrapper;
import com.example.Quiz.Application.Payload.QuizDTO;
import com.example.Quiz.Application.Repository.QuestionRepository;
import com.example.Quiz.Application.Repository.QuizRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;


    public QuizDTO createQuiz(String title, int numQ, String category) {

        List<Question> questions = this.questionRepository.getRandomQuestionsByCategory(numQ, category);    // main logic

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizRepository.save(quiz);

        return this.modelMapper.map(quiz , QuizDTO.class);
    }

    public void deleteQuiz(Integer quizId) {
        this.quizRepository.deleteById(quizId);
    }

    public List<QuestionWrapper> getQuizQuestionsByQuizId(Integer quizId) {

//        In quiz for user, Only give him/her questionId, questionTitle and 4 option which is abstract from Question class here
        Quiz quiz = this.quizRepository.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "Quiz Id", quizId));
        List<Question> questionsByDb = quiz.getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();  // One Quiz have multiple Questions

        for (Question q : questionsByDb){
            QuestionWrapper questionWrapper = new QuestionWrapper(q.getQuestionId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            questionsForUser.add(questionWrapper);
        }

        return questionsForUser;
    }


    public int calculateResult(Integer quizId, List<Response> responses) {
        Quiz quiz = this.quizRepository.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "Quiz Id", quizId));

//        I want to check each response of user to question's rightAnswer to give user score of quiz
        List<Question> questions = quiz.getQuestions();

        int right = 0;
        int i = 0;
        for (Response response : responses){
            if (response.getResponse().equals(questions.get(i).getRightAnswer())){
                right++;
            }
            i++;
        }
        return right;
    }

}
