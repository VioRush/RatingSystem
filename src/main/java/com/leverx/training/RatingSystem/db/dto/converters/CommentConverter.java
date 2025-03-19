package com.leverx.training.RatingSystem.db.dto.converters;

import com.leverx.training.RatingSystem.controller.CommentController;
import com.leverx.training.RatingSystem.db.dto.AdminCommentDTO;
import com.leverx.training.RatingSystem.db.dto.BaseCommentDTO;
import com.leverx.training.RatingSystem.db.dto.SellersCommentDTO;
import com.leverx.training.RatingSystem.db.dto.UsersCommentDTO;
import com.leverx.training.RatingSystem.db.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Component
public class CommentConverter implements Convertable<BaseCommentDTO, Comment> {
    private final UserConverter userConverter;
    private Boolean sellerscomments = true;
    private Boolean admin = false;

    public CommentConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public BaseCommentDTO convertToDTO(Comment entity) {
        if(admin){
            return new AdminCommentDTO(entity.getMessage(), entity.getCreatedAt(), entity.getRating(), entity.getApproved(),
                    userConverter.convertToDTO(entity.getSeller()), userConverter.convertToDTO(entity.getAuthor()))
                    .add(linkTo(methodOn(CommentController.class)
                            .getCommentById(entity.getSeller().getId(), entity.getId())).withSelfRel());
        }
        if(sellerscomments){
            return new SellersCommentDTO(entity.getMessage(),
                    entity.getCreatedAt(), entity.getRating(), userConverter.convertToDTO(entity.getAuthor()))
                    .add(linkTo(methodOn(CommentController.class)
                            .getCommentById(entity.getSeller().getId(), entity.getId())).withSelfRel());
        }
        return new UsersCommentDTO(entity.getMessage(), entity.getCreatedAt(), entity.getRating(), entity.getApproved(),
                userConverter.convertToDTO(entity.getSeller()))
                .add(linkTo(methodOn(CommentController.class)
                        .getCommentById(entity.getSeller().getId(), entity.getId())).withSelfRel());
    }

    @Override
    public Comment convertToEntity(BaseCommentDTO dto) {
        if(dto.getCreatedAt()==null){
            return new Comment(dto.getMessage(), dto.getRating());
        }
        return new Comment(dto.getMessage(), dto.getCreatedAt(), dto.getRating());
    }
}
