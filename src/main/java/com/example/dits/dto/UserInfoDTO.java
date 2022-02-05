package com.example.dits.dto;

import com.example.dits.entity.Role;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    private int userId;
    private String firstName;
    private String lastName;
    private String login;
    private RoleDTO role;

    //roles -> список ролей с , true false .

}
