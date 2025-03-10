package com.leverx.training.RatingSystem.db.model;

import com.leverx.training.RatingSystem.enums.Category;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "gameId")
    private List<GameObject> gameObjects;
}
