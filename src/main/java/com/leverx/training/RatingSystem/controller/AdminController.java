package com.leverx.training.RatingSystem.controller;

import com.leverx.training.RatingSystem.db.dto.BaseCommentDTO;
import com.leverx.training.RatingSystem.db.dto.BaseUserDTO;
import com.leverx.training.RatingSystem.db.dto.converters.CommentConverter;
import com.leverx.training.RatingSystem.db.dto.converters.UserConverter;
import com.leverx.training.RatingSystem.db.model.Comment;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.service.CommentService;
import com.leverx.training.RatingSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CommentService commentService;
    private final UserService userService;
    private final CommentConverter converter;
    private final UserConverter userConverter;

    @GetMapping({"/toApprove/comments", "/toApprove/comments/"})
    public ResponseEntity<List<BaseCommentDTO>> getAllComments(){
        List<Comment> comments = commentService.findNotApproved();
        converter.setAdmin(true);
        List<BaseCommentDTO> commentDTOList = comments.stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
        converter.setAdmin(false);
        return comments.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(commentDTOList);
    }

    @GetMapping({"/toApprove/sellers", "/toApprove/sellers/"})
    public ResponseEntity<List<BaseUserDTO>> getNotApprovedSellers(){
        List<User> users = userService.findNotApprovedSellers();
        return users.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(users.stream()
                .map(userConverter::convertToDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.findAll();
        if(users.isEmpty()) return ResponseEntity.badRequest().body("Users not found");
        List<BaseUserDTO> userDTOList = users.stream()
                .map(userConverter::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOList);
    }
}
