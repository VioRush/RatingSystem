package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO extends BaseCommentDTO{
    public CommentDTO(String message, double rating) {
        super(message, rating);
    }
}
