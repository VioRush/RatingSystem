package com.leverx.training.RatingSystem.db.dto.converters;

import com.leverx.training.RatingSystem.controller.UserController;
import com.leverx.training.RatingSystem.db.dto.*;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.enums.Role;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserConverter implements Convertable<BaseUserDTO, User> {
    @Override
    public BaseUserDTO convertToDTO(User entity) {
        if(entity.isAnonymous()){
            AnonymousUserDTO anonymousUserDTO = new AnonymousUserDTO(entity.getCreatedAt());
            anonymousUserDTO.add(linkTo(UserController.class).slash(entity.getId()).withSelfRel());
            return anonymousUserDTO;
        }
        if(entity.getRole().equals(Role.ADMINISTRATOR)) return null;
        UserDTO userDTO = new UserDTO(entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getCreatedAt(), entity.getRole(), entity.getAvgRating());
        userDTO.add(linkTo(UserController.class).slash(entity.getId()).withSelfRel());
        return userDTO;
    }

    @Override
    public User convertToEntity(BaseUserDTO dto) {
        if(dto.getClass() == UserCreationDTO.class){
            UserCreationDTO userCreationDTO = (UserCreationDTO) dto;
            return new User(userCreationDTO.getFirstName(), userCreationDTO.getLastName(),
                    userCreationDTO.getPassword(), userCreationDTO.getEmail(), userCreationDTO.getCreatedAt());
        }
        if(dto.getClass() == AnonymousUserDTO.class)
            return new User(dto.getCreatedAt());
        if(dto.getClass() == UserInCommentDTO.class) {
            UserInCommentDTO userDTO = (UserInCommentDTO) dto;
            return new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
        }
        UserDTO userDTO = (UserDTO) dto;
        return new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                userDTO.getCreatedAt(), userDTO.getRole(), userDTO.getAvgRating());
    }
}
