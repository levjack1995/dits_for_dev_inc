package com.example.dits.dto;

import com.example.dits.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestWithQuestionsDTO {
    private String name;
    private String description;
    private int testId;
    private List<Question> questions;
}
