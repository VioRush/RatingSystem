package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.model.Game;
import com.leverx.training.RatingSystem.db.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public List<Game> findAll(){
        return gameRepository.findAll();
    }

    public Optional<Game> getById(int id){
        return this.gameRepository.findById(id);
    }

    public void insert(Game game){
        this.gameRepository.save(game);
    }

    public void update(int id, Game game){
        if(getById(id).isPresent()) {
            Game g = getById(id).get();
            g.setTitle(game.getTitle());
            g.setCategory(game.getCategory());
            this.gameRepository.save(g);
        }
    }

    public void delete(int id){
        if(getById(id).isPresent()) this.gameRepository.delete(getById(id).get());
    }
}
