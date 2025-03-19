package com.leverx.training.RatingSystem.db.repository;

import com.leverx.training.RatingSystem.db.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
}