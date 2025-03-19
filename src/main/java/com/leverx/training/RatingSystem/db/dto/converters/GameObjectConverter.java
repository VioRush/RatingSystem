package com.leverx.training.RatingSystem.db.dto.converters;

import com.leverx.training.RatingSystem.controller.GameObjectController;
import com.leverx.training.RatingSystem.db.dto.GameObjectDTO;
import com.leverx.training.RatingSystem.db.model.GameObject;
import com.leverx.training.RatingSystem.db.model.User;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class GameObjectConverter implements Convertable<GameObjectDTO, GameObject>{

    private final GameConverter gameConverter;
    private final UserConverter userConverter;

    public GameObjectConverter(GameConverter gameConverter, UserConverter userConverter){
        this.gameConverter = gameConverter;
        this.userConverter = userConverter;
    }
    @Override
    public GameObjectDTO convertToDTO(GameObject entity) {
        GameObjectDTO objectDTO = new GameObjectDTO(entity.getTitle(), entity.getText(), entity.getType(), entity.getCreatedAt(),
                entity.getUpdatedAt(), userConverter.convertToDTO(entity.getUser()),
                gameConverter.convertToDTO(entity.getGame()));
        objectDTO.add(linkTo(GameObjectController.class).slash(entity.getId()).withSelfRel());
        return objectDTO;
    }

    @Override
    public GameObject convertToEntity(GameObjectDTO dto) {
        return new GameObject(dto.getTitle(), dto.getText(), dto.getType(), dto.getCreatedAt(),
                dto.getUpdatedAt(), userConverter.convertToEntity(dto.getUser()), gameConverter.convertToEntity(dto.getGame()));
    }
}
