package com.example.Quiz.Application.Service;

import com.example.Quiz.Application.Entity.Question;
import com.example.Quiz.Application.Payload.QuestionDTO;
import com.example.Quiz.Application.Exception.ResourceNotFoundException;
import com.example.Quiz.Application.Repository.QuestionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
