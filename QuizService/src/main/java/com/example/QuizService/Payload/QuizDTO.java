package com.example.QuizService.Payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class QuizDTO {

    private String categoryName;
    private Integer numQuestions;
    private String title;
}
