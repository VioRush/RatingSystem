package com.leverx.training.RatingSystem.db.dto;

import com.leverx.training.RatingSystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UserCreationDTO extends BaseUserDTO{
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
