package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Topic;
import com.example.dits.entity.User;

import java.util.List;

public interface UserService {
    void create(User user);
    void update(User user, int id);
    void delete(User user);
    void save(User user);
    List<User> findAll();
    User getUserByLogin(String login);
}
