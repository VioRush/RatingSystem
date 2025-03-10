package com.leverx.training.RatingSystem.db.repository;

import com.leverx.training.RatingSystem.db.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query("update User u set u.approved=true where u.id = ?1")
    void approve(int id);

    @Transactional
    @Modifying
    @Query("update User u set u.emailConfirmed=false where u.id = ?1")
    void confirmEmail(int id);
}