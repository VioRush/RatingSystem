package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.model.Comment;
import com.leverx.training.RatingSystem.db.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    public List<Comment> findBySeller(int sellerId){
        return commentRepository.findBySeller(sellerId);
    }

    public List<Comment> findByAuthor(int id){
        return commentRepository.findByAuthor(id);
    }

    public List<Comment> findNotApproved() { return commentRepository.findNotApproved(); }

    public Optional<Comment> getById(int id){
        return commentRepository.findById(id);
    }

    public void insert(Comment comment){
        this.commentRepository.save(comment);
    }

    public void update(int id, Comment comment){
        if(getById(id).isPresent()){
            Comment c = getById(id).get();
            c.setMessage(comment.getMessage());
            c.setRating(comment.getRating());
            commentRepository.save(c);
        }
    }

    public void approve(int id){
        commentRepository.approve(id);
    }

    public void delete(int id){
        if(getById(id).isPresent()) this.commentRepository.delete((Comment)getById(id).get());
    }

    public List<Comment> findBySellerApproved(int id) {
        return commentRepository.findBySellerApproved(id);
    }
}
