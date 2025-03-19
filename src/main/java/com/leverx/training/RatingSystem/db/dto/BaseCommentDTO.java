package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseCommentDTO extends RepresentationModel<BaseCommentDTO> {
    private String message;
    private Instant createdAt;
    private Double rating;

    public BaseCommentDTO(String message, double rating){
        this.message = message;
        this.rating = rating;
    }
}
