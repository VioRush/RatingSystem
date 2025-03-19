package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.model.GameObject;
import com.leverx.training.RatingSystem.db.repository.GameObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class GameObjectService {

    @Autowired
    private GameObjectRepository gameObjectRepository;

    public List<GameObject> findAll() {
        return gameObjectRepository.findAll();
    }

    public Optional<GameObject> getById(int id) {
        return gameObjectRepository.findById(id);
    }

    public void insert(GameObject gameObject){
        this.gameObjectRepository.save(gameObject);
    }

    public void update(int id, GameObject gameObject){
        if(getById(id).isPresent()) {
            GameObject object = getById(id).get();
            object.setTitle(gameObject.getTitle());
            object.setText(gameObject.getText());
            object.setType(gameObject.getType());
            object.setUpdatedAt(Instant.now());
            object.setGame(gameObject.getGame());
            this.gameObjectRepository.save(object);
        }
    }

    public void delete(int id){
        if(getById(id).isPresent()) this.gameObjectRepository.delete((GameObject) getById(id).get());
    }
}
