package com.example.dits.controllers;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.example.dits.mapper.UserInfoMapper;
import com.example.dits.service.RoleService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserEditorController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final UserInfoMapper userInfoMapper;

    @Autowired
    public UserEditorController(UserService userService, RoleService roleService, ModelMapper modelMapper, UserInfoMapper userInfoMapper) {
        passwordEncoder = new BCryptPasswordEncoder();
        this.userService = userService;
        this.roleService = roleService;
        this.userInfoMapper = userInfoMapper;
    }

    //В форме редактирования
    @PostMapping("/updateUser")
    public String editUser(@RequestParam String firstName, @RequestParam String lastName,
                           @RequestParam String roleName, @RequestParam String login,
                           @RequestParam String password, @RequestParam int id){

        User user = userService.getUserByUserId(id);
        Role role = roleService.getRoleByRoleName(roleName);

        if (!password.isEmpty()) {
            userService.updateUserWithPassword(user,id,firstName,lastName,role
            ,login,password);
            return "redirect:/user-editor";
        }

        userService.updateUser(user, id,firstName, lastName, role, login);

        return "redirect:/user-editor";

    }

    @ResponseBody
    @GetMapping("/editUser")
    public UserInfoDTO getUserInfo(@RequestParam int id){
        User user = userService.getUserByUserId(id);
        return userInfoMapper.convertToUserInfoDTO(user);
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam String firstName, @RequestParam String lastName,
                          @RequestParam String roleName, @RequestParam String login,
                          @RequestParam String password){

        Role role = roleService.getRoleByRoleName(roleName);
        User user = new User(firstName, lastName, login, role);
        userService.saveUserWithEncodedPassword(user,password);

        return "redirect:/user-editor";
    }

    @PostMapping("/removeUser")
    public String removeUser(@RequestParam int id){
        userService.deleteUserByUserId(id);
        return "redirect:/user-editor";
    }

}
