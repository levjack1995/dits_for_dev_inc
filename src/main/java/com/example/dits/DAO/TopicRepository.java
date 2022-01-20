package com.example.dits.DAO;

import com.example.dits.entity.Topic;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Integer> {
    Topic getTopicByName(String name);
}
