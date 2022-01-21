package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;
import com.example.dits.entity.Role;

import java.util.List;

public interface RoleService {
    void create(Role r);
    void update(Role r, int id);
    void delete(Role r);
    void save(Role r);
    List<Role> findAll();
    Role getRoleByRoleName(String roleName);
}
