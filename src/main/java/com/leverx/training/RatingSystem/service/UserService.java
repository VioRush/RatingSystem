package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.db.repository.UserRepository;
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
    @Autowired
    UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> getById(int id){
        return userRepository.findById(id);
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
}
