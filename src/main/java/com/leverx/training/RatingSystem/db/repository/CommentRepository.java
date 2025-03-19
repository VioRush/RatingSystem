package com.leverx.training.RatingSystem.db.repository;

import com.leverx.training.RatingSystem.db.model.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Transactional
    @Modifying
    @Query("update Comment c set c.approved=true where c.id = ?1")
    void approve(int id);

    @Query("select c from Comment c where c.seller.id = ?1")
    List<Comment> findBySeller(int id);

    @Query("select c from Comment c where c.seller.id = ?1 and c.approved = true")
    List<Comment> findBySellerApproved(int id);

    @Query("select c from Comment c where c.author.id = ?1")
    List<Comment> findByAuthor(int id);

    @Query("select c from Comment c where c.approved = false")
    List<Comment> findNotApproved();
}