package com.leverx.training.RatingSystem.db.model;

import com.leverx.training.RatingSystem.enums.Role;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.leverx.training.RatingSystem.enums.Role.USER;

@Data
@RequiredArgsConstructor
@Entity
@Table(name="user")
public class User implements UserDetails {
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
    private Boolean emailConfirmed;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @Column(name="avg_rating")
    private Double avgRating;

    @Column(name = "approved")
    private Boolean approved;

    @OneToMany(mappedBy = "user")
    private List<GameObject> gameObjects;

    @OneToMany(mappedBy = "author")
    private List<Comment> usersComments;

    @OneToMany(mappedBy = "seller")
    private List<Comment> comments;

    public User(String firstName, String lastName, String password, String email, Boolean emailConfirmed, Instant createdAt, Role role, Double avgRating, Boolean approved) {
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

    public User(String firstName, String lastName, String email, Instant createdAt, Role role, Double avgRating) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
        this.avgRating = avgRating;
    }

    public User(String firstName, String lastName, String password, String email, Instant createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = Instant.now();
    }

    public User(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAnonymous(){
        return role == USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
