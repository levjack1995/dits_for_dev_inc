package com.example.dits.service.impl;

import com.example.dits.DAO.QuestionRepository;
import com.example.dits.DAO.TestRepository;
import com.example.dits.entity.Question;
import com.example.dits.entity.Test;
import com.example.dits.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    @Transactional
    public List<Question> getQuestionsByTestName(String name){
        return questionRepository.getQuestionsByTestName(name);
    }

    @Transactional
    @Override
    public List<Question> getQuestionsByTest_TestId(int id) {
        return questionRepository.getQuestionsByTest_TestId(id);
    }

    @Transactional
    @Override
    public List<Question> getQuestionsByTest(Test test) {
        return questionRepository.getQuestionsByTest(test);
    }

    @Transactional
    @Override
    public Question getQuestionById(int id) {
        return questionRepository.getQuestionByQuestionId(id);
    }

    @Override
    public String getDescriptionFromQuestionList(List<Question> questionList, int index) {
        return questionList.get(index).getDescription();
    }

    @Transactional
    @Override
    public void addQuestion(String description, int testId) {
        Test test = testRepository.getTestByTestId(testId);
        new Question(description).setTest(test);
    }

    @Transactional
    @Override
    public void editQuestion(String description, int questionId) {
        Question questionByQuestionId = questionRepository.getQuestionByQuestionId(questionId);
        questionByQuestionId.setDescription(description);
    }

    @Transactional
    public void create(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public void update(Question question, int id) {
        Optional<Question> q = questionRepository.findById(id);
        if(q.isEmpty())
            return;
        else
            questionRepository.save(question);
    }

    @Transactional
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    @Transactional
    public void save(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public List<Question> findAll() {
        return questionRepository.findAll();
    }


}
