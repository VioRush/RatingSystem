package com.leverx.training.RatingSystem.controller;

import com.leverx.training.RatingSystem.db.dto.GameDTO;
import com.leverx.training.RatingSystem.db.dto.converters.GameConverter;
import com.leverx.training.RatingSystem.db.model.Game;
import com.leverx.training.RatingSystem.service.GameService;
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
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final GameConverter gameConverter;

    @GetMapping({"","/"})
    public ResponseEntity<List<GameDTO>> getAll(){
        List<Game> games = gameService.findAll();
        List<GameDTO> gameDTOList = games.stream()
                .map(gameConverter::convertToDTO)
                .collect(Collectors.toList());
        return games.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(gameDTOList);
    }

    @PostMapping("/")
    public ResponseEntity<EntityModel<GameDTO>> addGame(@RequestBody GameDTO gameDTO){
        Game game = gameConverter.convertToEntity(gameDTO);
        gameService.insert(game);
        gameDTO=gameConverter.convertToDTO(game)
                .add(linkTo(methodOn(GameController.class).getAll()).withRel("to all games"));
        return ResponseEntity.created(gameDTO.getRequiredLink("self").toUri())
                .body(EntityModel.of(gameDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GameDTO>> getGameById(@PathVariable("id") int id){
        try {
            Game game = gameService.getById(id).get();
            GameDTO gameConverted = gameConverter.convertToDTO(game)
                    .add(linkTo(methodOn(GameController.class).getAll()).withRel("to all games"));
            return ResponseEntity.ok(EntityModel.of(gameConverted));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<EntityModel<GameDTO>> editGame(@PathVariable("id") int id, @RequestBody GameDTO gameDTO){
        Game game = gameConverter.convertToEntity(gameDTO);
        gameService.update(id, game);
        gameDTO = gameConverter.convertToDTO(game)
                .add(linkTo(methodOn(GameController.class).getAll()).withRel("to all games"));
        return ResponseEntity.ok(EntityModel.of(gameDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> deleteGame(@PathVariable("id") int id){
        try{
            gameService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
