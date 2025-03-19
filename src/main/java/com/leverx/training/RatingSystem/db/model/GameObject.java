package com.leverx.training.RatingSystem.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "game_object")
public class GameObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public GameObject(String title, String text, String type, Instant createdAt, User user, Game game) {
        this.title = title;
        this.text = text;
        this.type = type;
        this.createdAt = createdAt;
        this.user = user;
        this.game = game;
    }

    public GameObject(String text, String title, String type, Instant createdAt, Instant updatedAt, User user, Game game) {
        this.text = text;
        this.title = title;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.game = game;
    }
}
