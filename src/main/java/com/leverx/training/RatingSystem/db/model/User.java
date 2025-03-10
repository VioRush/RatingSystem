package com.leverx.training.RatingSystem.db.model;

import com.leverx.training.RatingSystem.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name="role")
    private Role role;

    @Column(name="avg_rating")
    private double avgRating;

    @Column(name = "approved")
    private boolean approved;

    @OneToMany(mappedBy = "user")
    private List<GameObject> gameObjects;

    @OneToMany(mappedBy = "author")
    private List<Comment> usersComments;

    @OneToMany(mappedBy = "seller")
    private List<Comment> comments;

    public User(String firstName, String lastName, String password, String email, boolean emailConfirmed, Instant createdAt, Role role, double avgRating, boolean approved) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.createdAt = createdAt;
        this.role = role;
        this.avgRating = avgRating;
        this.approved = approved;
    }
}
