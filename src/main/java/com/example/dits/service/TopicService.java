package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TopicService {
    public void create(Topic topic);
    public void update(Topic topic, int id);
    public void delete(Topic topic);
    public void save(Topic topic);
    public List<Topic> findAll();
    Topic getTopicByName(String name);
}
