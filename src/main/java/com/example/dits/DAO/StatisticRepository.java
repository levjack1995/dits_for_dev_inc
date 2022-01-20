package com.example.dits.DAO;

import com.example.dits.entity.Statistic;
import com.example.dits.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic,Integer> {
    List<Statistic> getStatisticsByUser(User user);
}
