package com.leverx.training.RatingSystem.db.repository;

import com.leverx.training.RatingSystem.db.model.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameObjectRepository extends JpaRepository<GameObject, Integer> {
}