package com.leverx.training.RatingSystem.db.dto;

import com.leverx.training.RatingSystem.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class GameDTO extends RepresentationModel<GameDTO> {

    private String title;
    private Category category;
}
