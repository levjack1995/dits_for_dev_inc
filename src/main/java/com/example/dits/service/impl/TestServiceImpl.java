package com.example.dits.service.impl;

import com.example.dits.DAO.TestRepository;
import com.example.dits.entity.Answer;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import com.example.dits.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

   private final TestRepository repository;

   @Transactional
   public void create(Test test) {
      repository.save(test);
   }

   @Transactional
   public void update(Test test, int id) {
      Optional<Test> t = repository.findById(id);
      if(t.isEmpty())
         return;
      else
         repository.save(test);
   }

   @Transactional
   public void delete(Test test) {
      repository.delete(test);
   }

   @Transactional
   public void save(Test test) {
      repository.save(test);
   }

   @Transactional
   public List<Test> findAll() {
      return repository.findAll();
   }

   @Transactional
   @Override
   public List<Test> getTestsByTopic(Topic topic) {
      return repository.getTestsByTopic(topic);
   }

}
