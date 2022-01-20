package com.example.dits.DAO;

import com.example.dits.entity.Question;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {

    @Query("select q from Question q where q.test.name = ?1")
    List<Question> getQuestionsByTestName(String name);
}
