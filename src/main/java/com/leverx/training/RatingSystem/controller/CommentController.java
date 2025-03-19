package com.leverx.training.RatingSystem.controller;

import com.leverx.training.RatingSystem.db.dto.*;
import com.leverx.training.RatingSystem.db.dto.converters.CommentConverter;
import com.leverx.training.RatingSystem.db.dto.converters.UserConverter;
import com.leverx.training.RatingSystem.db.model.Comment;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.enums.Role;
import com.leverx.training.RatingSystem.service.CommentService;
import com.leverx.training.RatingSystem.service.JWTService;
import com.leverx.training.RatingSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final CommentConverter converter;
    private final UserConverter userConverter;
    private final JWTService jwtService;

    @GetMapping({"/users/{userId}/comments","/users/{userId}/comments/"})
    public ResponseEntity<List<BaseCommentDTO>> getAll(@PathVariable("userId") int userId){
        if(userService.getById(userId).get().getRole() == Role.SELLER)
        {
            List<Comment> comments = commentService.findBySellerApproved(userId);
            List<BaseCommentDTO> commentDTOList = comments.stream()
                    .map(converter::convertToDTO)
                    .collect(Collectors.toList());
            return comments.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(commentDTOList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping({"/comments/own", "/comments/own/"})
    public ResponseEntity<?> getAllUsersComments(@RequestHeader("Authorization") String token){
        try {
            if(token.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            int loggedUserId = userService.getByEmail(jwtService.extractEmail(token)).get().getId();
            List<Comment> comments = commentService.findByAuthor(loggedUserId);
            List<BaseCommentDTO> commentDTOList = comments.stream()
                    .map(converter::convertToDTO)
                    .collect(Collectors.toList());
            return comments.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(commentDTOList);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/comments/")
    public ResponseEntity<?> addComment(@PathVariable("userId") int userId, @RequestBody CommentDTO commentDTO,
                                        @RequestHeader(value = "Authorization", required = false) String token){
        try {
            Comment comment = converter.convertToEntity(commentDTO);
            Optional<User> seller = userService.getById(userId);
            if (seller.isEmpty()) return ResponseEntity.notFound().build();
            comment.setSeller(seller.get());
            User user;
            if(token!=null){
                user = userService.getByEmail(jwtService.extractEmail(token)).get();
            } else {
                user = new User(Instant.now());
                user.setRole(Role.USER);
                userService.insert(user);
            }
            comment.setAuthor(user);
            comment.setApproved(false);
            commentService.insert(comment);
            BaseCommentDTO objectDTO = converter.convertToDTO(comment)
                    .add(linkTo(methodOn(CommentController.class).getAll(userId)).withRel("to all comments"));
            return ResponseEntity.created(objectDTO.getRequiredLink("self").toUri())
                    .body(EntityModel.of(objectDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/comments/")
    public ResponseEntity<?> addCommentAndSeller(@RequestBody CommentWithSellerDTO commentDTO,
                                                 @RequestHeader(value = "Authorization", required = false) String token){
        try {
            BaseUserDTO userDTO = commentDTO.getSeller();
            User user = userConverter.convertToEntity(userDTO);
            user.setRole(Role.SELLER);
            user.setAvgRating(0.0);
            user.setApproved(false);
            userService.insert(user);
            int sellerId = user.getId();
            Comment comment = converter.convertToEntity(commentDTO);
            comment.setSeller(user);
            if(token!=null){
                user = userService.getByEmail(jwtService.extractEmail(token)).get();
            } else {
                user = new User(Instant.now());
                user.setRole(Role.USER);
                userService.insert(user);
            }
            comment.setAuthor(user);
            comment.setApproved(false);
            commentService.insert(comment);
            BaseCommentDTO objectDTO = converter.convertToDTO(comment)
                    .add(linkTo(methodOn(CommentController.class).getAll(sellerId)).withRel("to all comments"));
            return ResponseEntity.created(objectDTO.getRequiredLink("self").toUri())
                    .body(EntityModel.of(objectDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/comments/{id}")
    public ResponseEntity<EntityModel<BaseCommentDTO>> getCommentById(@PathVariable("userId") int userId, @PathVariable("id") int id){
        try {
            Comment comment = commentService.getById(id).get();
            if (userService.getById(userId).get().isAnonymous()) {
                converter.setSellerscomments(false);
            } else {
                converter.setSellerscomments(true);
            }
            BaseCommentDTO commentDTO = converter.convertToDTO(comment);
            commentDTO.add(linkTo(methodOn(CommentController.class).getAll(userId)).withRel("to all comments"));
            return ResponseEntity.ok(EntityModel.of(commentDTO));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{userId}/comments/{id}")
    public ResponseEntity<EntityModel<BaseCommentDTO>> editComment(@PathVariable("userId") int userId, @PathVariable("id") int id, @RequestBody BaseCommentDTO baseCommentDTO){
        Comment comment = converter.convertToEntity(baseCommentDTO);
        commentService.update(id, comment);
        baseCommentDTO = converter.convertToDTO(comment)
                .add(linkTo(methodOn(CommentController.class).getAll(userId)).withRel("to all comments"));
        return ResponseEntity.ok(EntityModel.of(baseCommentDTO));
    }

    @PutMapping("/users/{userId}/comments/{id}/approve")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> approveComment(@PathVariable("userId") int userId, @PathVariable("id") int id){
        System.out.println("here");
        try {
            commentService.approve(id);
            userService.updateRating(userId);
            return ResponseEntity.ok().body("The comment is approved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{userId}/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") int id){
        try{
            commentService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
