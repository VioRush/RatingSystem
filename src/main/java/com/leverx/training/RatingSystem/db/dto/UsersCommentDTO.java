package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UsersCommentDTO extends BaseCommentDTO{

    private Boolean approved;
    private BaseUserDTO seller;

    public UsersCommentDTO(String message, Instant createdAt, double rating, boolean approved, BaseUserDTO seller) {
        super(message, createdAt, rating);
        this.approved = approved;
        this.seller = seller;
    }
}
