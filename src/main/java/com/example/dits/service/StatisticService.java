package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Role;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.User;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    public void create(Statistic st);
    public void update(Statistic st, int id);
    public void delete(Statistic st);
    public void save(Statistic st);
    public List<Statistic> findAll();
    void saveMapOfStat(Map<String, Statistic> map, String endTest);
    List<Statistic> getStatisticsByUser(User user);
}
