package com.leverx.training.RatingSystem.controller;

import com.leverx.training.RatingSystem.db.dto.BaseUserDTO;
import com.leverx.training.RatingSystem.db.dto.converters.UserConverter;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.enums.Category;
import com.leverx.training.RatingSystem.enums.Role;
import com.leverx.training.RatingSystem.service.JWTService;
import com.leverx.training.RatingSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter converter;
    private final JWTService jwtService;

    @GetMapping({"","/"})
    public ResponseEntity<List<BaseUserDTO>> getAll(){
        List<User> users = userService.findAllApprovedSellers();
        List<BaseUserDTO> userDTOList = users.stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
        return users.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BaseUserDTO>> getUserById(@PathVariable("id") int id){
        try {
            User user = userService.getById(id).get();
            BaseUserDTO userDTO = converter.convertToDTO(user)
                    .add(linkTo(methodOn(UserController.class).getAll()).withRel("to all users"),
                            linkTo(methodOn(CommentController.class).getAll(id)).withRel("comments"));
            return ResponseEntity.ok(EntityModel.of(userDTO));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<BaseUserDTO>> getRankings(@RequestParam(value = "limit", required = false, defaultValue = "3") int limit){
        List<User> users = userService.getRanking(limit);
        List<BaseUserDTO> userDTOList = users.stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
        return users.isEmpty()?ResponseEntity.ok().build():ResponseEntity.ok(userDTOList);
    }

    @PostMapping("/")
    public ResponseEntity<?> addSellerProfile(@RequestHeader("Authorization") String token) {
        try {
            User user = userService.getByEmail(jwtService.extractEmail(token)).get();
            int loggedUserId = user.getId();
            user.setRole(Role.SELLER);
            user.setApproved(false);
            userService.update(loggedUserId, user);
            return ResponseEntity.ok("Your request of creating profile is sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable("id") int id, @RequestBody BaseUserDTO userDTO,
                                                             @RequestHeader("Authorization") String token){
        if(token.isEmpty()) return ResponseEntity.badRequest().body("Only authenticate users can edit their details");
        int loggedUserId = userService.getByEmail(jwtService.extractEmail(token)).get().getId();
        if(loggedUserId != id) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = converter.convertToEntity(userDTO);
        userService.update(id, user);
        userDTO = converter.convertToDTO(user)
                .add(linkTo(methodOn(UserController.class).getAll()).withRel("to all users"));
        return ResponseEntity.ok(EntityModel.of(userDTO));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> approveUser(@PathVariable("id") int id){
        try {
            User user = userService.getById(id).get();
            userService.approve(id);
            user.setAvgRating(0.0);
            userService.update(id, user);
            return ResponseEntity.ok("The seller has been approved!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id, @RequestHeader("Authorization") String token){
        try{
            if(token.isEmpty()) return ResponseEntity.badRequest().body("Only authenticate users can delete their profiles");
            int loggedUserId = userService.getByEmail(jwtService.extractEmail(token)).get().getId();
            if(loggedUserId != id) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
