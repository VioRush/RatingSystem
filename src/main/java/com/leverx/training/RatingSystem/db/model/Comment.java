package com.leverx.training.RatingSystem.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@RequiredArgsConstructor
@Entity
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "approved", nullable = false)
    private Boolean approved;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    public Comment(String message, Double rating) {
        this.message = message;
        this.rating = rating;
        this.setApproved(false);
        this.setCreatedAt(Instant.now());
    }

    public Comment(String message, Instant createdAt, Double rating) {
        this.createdAt = createdAt;
        this.message = message;
        this.rating = rating;
    }
}
