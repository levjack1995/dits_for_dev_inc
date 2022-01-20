package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;

import java.util.List;

public interface QuestionService {
    public void create(Question q);
    public void update(Question q, int id);
    public void delete(Question q);
    public void save(Question q);
    public List<Question> findAll();
    List<Question> getQuestionsByTestName(String name);
}
