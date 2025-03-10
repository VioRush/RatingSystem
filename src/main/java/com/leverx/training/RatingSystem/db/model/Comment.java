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
    private boolean approved;

    @Column(name = "rating", nullable = false)
    private double rating;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
}
