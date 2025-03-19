package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CommentWithSellerDTO extends BaseCommentDTO{
    private UserInCommentDTO seller;

    public CommentWithSellerDTO(String message, double rating, UserInCommentDTO userDTO) {
        super(message, rating);
        this.seller = userDTO;
    }
}
