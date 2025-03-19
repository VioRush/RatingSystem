package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.model.Comment;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.db.repository.UserRepository;
import com.leverx.training.RatingSystem.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter
public class UserService {

    private final UserRepository userRepository;
    private final CommentService commentService;

    public List<User> findAll(){
        return userRepository.findAllUsers();
    }

    public List<User> findNotApprovedSellers() { return userRepository.findNotApprovedSellers(); }

    public List<User> findAllApprovedSellers() { return userRepository.findApprovedSellers(); }

    public Optional<User> getById(int id){
        return userRepository.findById(id);
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void insert(User user){
        this.userRepository.save(user);
    }

    public void update(int id, User user){
        if(getById(id).isPresent()){
            User u = getById(id).get();
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEmail(user.getEmail());
            u.setAvgRating(user.getAvgRating());
            userRepository.save(u);
        }
    }

    public void approve(int id){
        userRepository.approve(id);
    }

    public void confirmEmail(int id){
        userRepository.confirmEmail(id);
    }

    public void delete(int id){
        if(getById(id).isPresent()) this.userRepository.delete((User)getById(id).get());
    }

    public void updateRating(int id) {
        if(getById(id).isEmpty()) throw new RuntimeException("The seller is not found. Couldn't update rating");
        User u = getById(id).get();
        List<Comment> comments = commentService.findBySellerApproved(id);
        double avgRating = comments.stream().mapToDouble(Comment::getRating).average().orElse(0.0);
        u.setAvgRating(avgRating);
        userRepository.save(u);
    }

    public List<User> getRanking(int limit) {
        return userRepository.getTopSellers(limit);
    }
}
