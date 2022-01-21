package com.example.dits.controllers;

import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.example.dits.service.RoleService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserEditorController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserEditorController(UserService userService, RoleService roleService) {
        passwordEncoder = new BCryptPasswordEncoder();
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/editUser")
    public String editUser(@RequestParam String firstName, @RequestParam String lastName,
                           @RequestParam String roleName, @RequestParam String login,
                           @RequestParam String password, @RequestParam int userId){

        User user = userService.getUserByUserId(userId);

        if (!password.isEmpty()) {
            changePassword(password, user);
        }

        Role role = roleService.getRoleByRoleName(roleName);
        changeUser(firstName,lastName,role,login,user);
        userService.update(user,userId);
        System.out.println(firstName + lastName + roleName + login + password);

        return "/admin";

    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam String firstName, @RequestParam String lastName,
                          @RequestParam String roleName, @RequestParam String login,
                          @RequestParam String password){

        List<Role> roleList = getRoleList(roleName);
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(firstName, lastName, login, encodedPassword, roleList);
        userService.save(user);

        return "/admin";
    }

    @GetMapping("/removeUser")
    public String removeUser(@RequestParam int userId){
        System.out.println(userId);
        userService.deleteUserByUserId(userId);
        return "/admin";
    }

    private List<Role> getRoleList(String roleName) {
        Role role = roleService.getRoleByRoleName(roleName);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        return roleList;
    }

    private void changePassword(String password, User user) {
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }

    private void changeUser (String firstName, String lastName,
                             Role role, String login, User user){
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.getRoles().set(0,role);
        user.setLogin(login);
    }

}
