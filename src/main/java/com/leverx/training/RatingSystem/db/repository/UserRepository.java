package com.leverx.training.RatingSystem.db.repository;

import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.approved=true where u.id = ?1")
    void approve(int id);

    @Transactional
    @Modifying
    @Query("update User u set u.emailConfirmed=true where u.id = ?1")
    void confirmEmail(int id);

    @Query("select u from User u where u.role = Role.SELLER and u.approved = false")
    List<User> findNotApprovedSellers();

    @Query("select u from User u where u.role = Role.SELLER and u.approved = true")
    List<User> findApprovedSellers();

    @Query("select u from User u where u.role = Role.SELLER or u.role = Role.USER")
    List<User> findAllUsers();

    @Query("select u from User u where u.role = Role.SELLER order by u.avgRating DESC limit ?1")
    List<User> getTopSellers(int limit);

}