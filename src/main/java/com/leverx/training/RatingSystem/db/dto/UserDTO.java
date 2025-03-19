package com.leverx.training.RatingSystem.db.dto;

import com.leverx.training.RatingSystem.db.model.Comment;
import com.leverx.training.RatingSystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO extends BaseUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Double avgRating;

    public UserDTO(String firstName, String lastName, String email, Instant createdAt, Role role, double avgRating) {
        super(createdAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.avgRating = avgRating;
    }

    public UserDTO(String firstName, String lastName, String email) {
        super(Instant.now());
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
