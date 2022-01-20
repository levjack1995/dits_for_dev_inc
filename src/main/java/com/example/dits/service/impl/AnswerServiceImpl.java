package com.example.dits.service.impl;

import com.example.dits.DAO.AnswerRepository;
import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;
import com.example.dits.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository repo;

    @Transactional
    public void create(Answer a) {
        repo.save(a);
    }

    @Transactional
    public void update(Answer a, int id) {
        Optional<Answer> answer = repo.findById(id);
        if(answer.isEmpty())
            return;
        else
            repo.save(a);
    }

    @Transactional
    public void delete(Answer a) {
        repo.delete(a);
    }

    @Transactional
    public void save(Answer a) {
        repo.save(a);
    }

    @Transactional
    public List<Answer> findAll() {
        return repo.findAll();
    }

    @Transactional
    public List<Answer> getAnswersByQuestion(Question question){
        return repo.getAnswersByQuestion(question);
    }
}
