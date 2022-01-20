package com.example.dits.service.impl;

import com.example.dits.DAO.UserRepository;
import com.example.dits.entity.Question;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.User;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;


    @Transactional
    public void create(User user) {
        repository.save(user);
    }

    @Transactional
    public void update(User user, int id) {
        Optional<User> us = repository.findById(id);
        if(us.isEmpty())
            return;
        else
            repository.save(user);
    }

    @Transactional
    public void delete(User user) {
        repository.delete(user);
    }

    @Transactional
    public void save(User user) {
        repository.save(user);
    }

    @Transactional
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public User getUserByLogin(String login){
        return repository.getUserByLogin(login);
    }
}
