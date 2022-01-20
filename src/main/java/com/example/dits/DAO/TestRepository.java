package com.example.dits.DAO;

import com.example.dits.entity.Test;
import com.example.dits.entity.Topic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test,Integer> {
    List<Test> getTestsByTopic(Topic topic);
}
