package com.example.dits.DAO;

import com.example.dits.entity.Question;
import com.example.dits.entity.Test;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {

    List<Question> getQuestionsByTestName(String name);
    List<Question> getQuestionsByTest_TestId(int id);
    List<Question> getQuestionsByTest(Test test);
    Question getQuestionByQuestionId(int id);
}
