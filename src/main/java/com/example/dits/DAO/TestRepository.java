package com.example.dits.DAO;

import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends JpaRepository<Test,Integer> {
    List<Test> getTestsByTopic(Topic topic);
    List<Test> getTestsByTopicName(String name);
    List<Test> getTestsByTopic_TopicId(int topicId);
    Test getTestByTestId(int id);
    void removeTestByTestId(int id);
}
