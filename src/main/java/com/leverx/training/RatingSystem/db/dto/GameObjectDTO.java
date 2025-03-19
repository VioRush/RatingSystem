package com.leverx.training.RatingSystem.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@AllArgsConstructor
public class GameObjectDTO extends RepresentationModel<GameObjectDTO> {

    private String title;
    private String text;
    private String type;
    private Instant createdAt;
    private Instant updatedAt;
    private BaseUserDTO user;
    private GameDTO game;
}
