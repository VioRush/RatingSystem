package com.leverx.training.RatingSystem.db.dto;

import lombok.Data;

@Data
public class UserInCommentDTO extends BaseUserDTO{
    private String firstName;
    private String lastName;
    private String email;

    public UserInCommentDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
