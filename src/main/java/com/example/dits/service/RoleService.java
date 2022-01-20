package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;
import com.example.dits.entity.Role;

import java.util.List;

public interface RoleService {
    public void create(Role r);
    public void update(Role r, int id);
    public void delete(Role r);
    public void save(Role r);
    public List<Role> findAll();
}
