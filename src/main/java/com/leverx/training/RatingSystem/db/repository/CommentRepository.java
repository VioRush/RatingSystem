package com.leverx.training.RatingSystem.db.repository;

import com.leverx.training.RatingSystem.db.model.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Transactional
    @Modifying
    @Query("update Comment c set c.approved=true where c.id = ?1")
    void approve(int id);
}