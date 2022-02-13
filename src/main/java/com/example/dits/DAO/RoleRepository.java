package com.example.dits.DAO;

import com.example.dits.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role getRoleByRoleName(String roleName);
}
