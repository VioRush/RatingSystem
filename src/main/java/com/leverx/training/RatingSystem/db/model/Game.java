package com.leverx.training.RatingSystem.db.model;

import com.leverx.training.RatingSystem.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "game")
    private List<GameObject> gameObjects;

    public Game(String title, Category category) {
        this.title = title;
        this.category = category;
    }
}
