package com.example.dits.DAO;

import com.example.dits.entity.Role;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
