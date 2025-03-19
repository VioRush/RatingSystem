package com.leverx.training.RatingSystem.db.dto.converters;

import com.leverx.training.RatingSystem.controller.GameController;
import com.leverx.training.RatingSystem.db.dto.GameDTO;
import com.leverx.training.RatingSystem.db.model.Game;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class GameConverter implements Convertable<GameDTO, Game>{

    @Override
    public GameDTO convertToDTO(Game entity) {
        return new GameDTO(entity.getTitle(), entity.getCategory())
                .add(linkTo(GameController.class).slash(entity.getId()).withSelfRel());
    }

    @Override
    public Game convertToEntity(GameDTO dto) {
        return new Game(dto.getTitle(), dto.getCategory());
    }
}
