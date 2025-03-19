package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SellersCommentDTO extends BaseCommentDTO {

    private BaseUserDTO author;

    public SellersCommentDTO(String message, Instant createdAt, double rating, BaseUserDTO author) {
        super(message, createdAt, rating);
        this.author = author;
    }
}
