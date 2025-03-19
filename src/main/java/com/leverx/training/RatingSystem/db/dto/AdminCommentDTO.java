package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AdminCommentDTO extends BaseCommentDTO{

    private Boolean approved;
    private BaseUserDTO seller;
    private BaseUserDTO author;

    public AdminCommentDTO(String message, Instant createdAt, Double rating, Boolean approved, BaseUserDTO seller, BaseUserDTO author) {
        super(message, createdAt, rating);
        this.approved = approved;
        this.seller = seller;
        this.author = author;
    }
}
