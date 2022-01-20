package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;

import java.util.List;

public interface TestService {
     void create(Test test);
     void update(Test test, int id);
     void delete(Test test);
     void save(Test test);
     List<Test> findAll();
     List<Test> getTestsByTopic(Topic topic);
}
