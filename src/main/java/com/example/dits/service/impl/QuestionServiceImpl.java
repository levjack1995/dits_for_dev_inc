package com.example.dits.service.impl;

import com.example.dits.DAO.QuestionRepository;
import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;
import com.example.dits.entity.Test;
import com.example.dits.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository repo;

    @Transactional
    public List<Question> getQuestionsByTestName(String name){
        return repo.getQuestionsByTestName(name);
    }

    @Transactional
    @Override
    public List<Question> getQuestionsByTest_TestId(int id) {
        return repo.getQuestionsByTest_TestId(id);
    }

    @Transactional
    @Override
    public List<Question> getQuestionsByTest(Test test) {
        return repo.getQuestionsByTest(test);
    }

    @Override
    public String getDescriptionFromQuestionList(List<Question> questionList, int index) {
        return questionList.get(index).getDescription();
    }

    @Transactional
    public void create(Question question) {
        repo.save(question);
    }

    @Transactional
    public void update(Question question, int id) {
        Optional<Question> q = repo.findById(id);
        if(q.isEmpty())
            return;
        else
            repo.save(question);
    }

    @Transactional
    public void delete(Question question) {
        repo.delete(question);
    }

    @Transactional
    public void save(Question question) {
        repo.save(question);
    }

    @Transactional
    public List<Question> findAll() {
        return repo.findAll();
    }
}
