package com.leverx.training.RatingSystem.controller;

import com.leverx.training.RatingSystem.db.dto.GameObjectDTO;
import com.leverx.training.RatingSystem.db.dto.converters.GameObjectConverter;
import com.leverx.training.RatingSystem.db.model.GameObject;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.enums.Role;
import com.leverx.training.RatingSystem.service.GameObjectService;
import com.leverx.training.RatingSystem.service.JWTService;
import com.leverx.training.RatingSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/objects")
public class GameObjectController {

    private final GameObjectService gameObjectService;
    private final GameObjectConverter converter;
    private final UserService userService;
    private final JWTService jwtService;

    @GetMapping({"","/"})
    public ResponseEntity<List<GameObjectDTO>> getAll(){
        List<GameObject> objects = gameObjectService.findAll();
        List<GameObjectDTO> objectDTOList = objects.stream()
                .map(converter::convertToDTO).collect(Collectors.toList());
        return objects.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(objectDTOList);
    }

    @PostMapping("/")
    public ResponseEntity<?> addGameObject(@RequestBody GameObjectDTO gameObjectDTO, @RequestHeader("Authorization") String token){
        if(token.isEmpty()) return ResponseEntity.badRequest().body("Only authenticate sellers can create new game objects");
        User user = userService.getByEmail(jwtService.extractEmail(token)).get();
        if(!(user.getRole().equals(Role.SELLER))) return ResponseEntity.badRequest().body("Only sellers can add game objects");
        GameObject object = converter.convertToEntity(gameObjectDTO);
        object.setUser(user);
        gameObjectService.insert(object);
        GameObjectDTO objectDTO = converter.convertToDTO(object)
                .add(linkTo(methodOn(GameObjectController.class).getAll()).withRel("to all game objects"));
        return ResponseEntity.created(objectDTO.getRequiredLink("self").toUri())
                .body(EntityModel.of(objectDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GameObjectDTO>> getGameObjectById(@PathVariable("id") int id){
        try {
            GameObject object = gameObjectService.getById(id).get();
            GameObjectDTO dto = converter.convertToDTO(object);
            dto.add(linkTo(methodOn(GameObjectController.class).getAll()).withRel("to all game objects"));
            return ResponseEntity.ok(EntityModel.of(dto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editGameObject(@PathVariable("id") int id, @RequestBody GameObjectDTO gameObjectDTO,
                                                                     @RequestHeader("Authorization") String token){
        if(token.isEmpty()) return ResponseEntity.badRequest().body("Please log in");
        if(userService.getByEmail(jwtService.extractEmail(token)).get().getId() != id)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        GameObject object = converter.convertToEntity(gameObjectDTO);
        gameObjectService.update(id, object);
        gameObjectDTO = converter.convertToDTO(object)
                .add(linkTo(methodOn(GameObjectController.class).getAll()).withRel("to all game objects"));
        return ResponseEntity.ok(EntityModel.of(gameObjectDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGameObject(@PathVariable("id") int id, @RequestHeader("Authorization") String token){
        try{
            if(token.isEmpty()) return ResponseEntity.badRequest().body("Please log in");
            if(userService.getByEmail(jwtService.extractEmail(token)).get().getId() != id)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            gameObjectService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
