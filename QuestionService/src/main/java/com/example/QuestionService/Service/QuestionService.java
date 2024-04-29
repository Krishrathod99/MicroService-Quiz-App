package com.example.QuestionService.Service;


import com.example.QuestionService.Entity.Question;
import com.example.QuestionService.Entity.Response;
import com.example.QuestionService.Exception.ResourceNotFoundException;
import com.example.QuestionService.Payload.QuestionDTO;
import com.example.QuestionService.Payload.QuestionWrapper;
import com.example.QuestionService.Repository.QuestionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;


    public QuestionDTO createQuestion(QuestionDTO questionDTO){
        Question question = this.modelMapper.map(questionDTO, Question.class);
        Question savedQuestion = questionRepository.save(question);
        return this.modelMapper.map(savedQuestion , QuestionDTO.class);
    }

    public QuestionDTO updateQuestion(QuestionDTO questionDTO , Integer questionId){
        Question question = this.questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Question Id", questionId));

        question.setQuestionTitle(questionDTO.getQuestionTitle());
        question.setCategory(questionDTO.getCategory());
        question.setOption1(questionDTO.getOption1());
        question.setOption2(questionDTO.getOption2());
        question.setOption3(questionDTO.getOption3());
        question.setOption4(questionDTO.getOption4());
        question.setRightAnswer(questionDTO.getRightAnswer());
        question.setDifficultyLevel(questionDTO.getDifficultyLevel());

        this.questionRepository.save(question);
        return this.modelMapper.map(question , QuestionDTO.class);
    }

    public void deleteQuestion(Integer questionId){
        Question question = this.questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "Question Id", questionId));
        questionRepository.delete(question);

    }
    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = this.questionRepository.findAll();
        return questions.stream().map(question -> this.modelMapper.map(question , QuestionDTO.class)).toList();
    }

    public List<Question> getQuestionsByCategory(String category){
        return this.questionRepository.findByCategory(category);
    }

    public List<Integer> getQuestionsForQuiz(String category, int totalQuestions) {
        List<Integer> questions = this.questionRepository.getRandomQuestionsByCategory(category,totalQuestions);
        return questions;
    }

    public List<QuestionWrapper> getQuestionsFromId(List<Integer> questionsId) {

        List<Question> questions = new ArrayList<>();
        for (Integer id : questionsId){
            questions.add(this.questionRepository.findById(id).get());
        }

        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        for (Question q : questions){
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setQuestionId(q.getQuestionId());
            wrapper.setQuestionTitle(q.getQuestionTitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            questionWrappers.add(wrapper);
        }
        return questionWrappers;
    }

    public Integer getScore(List<Response> responses) {

        int i = 0;
        int right = 0;
        for (Response response : responses){
            Question question = this.questionRepository.findById(response.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Question","QuestionId", response.getId()));
            if (response.getResponse().equals(question.getRightAnswer())){
                right++;
            }
            i++;
        }
        return right;
    }
}
