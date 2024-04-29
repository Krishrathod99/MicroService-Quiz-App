package com.example.Quiz.Application.Payload;

import com.example.Quiz.Application.Entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuizDTO {

    private int id;
    private String title;
    private List<Question> questions;
}
