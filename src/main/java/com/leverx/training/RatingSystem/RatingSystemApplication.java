package com.leverx.training.RatingSystem;

import com.leverx.training.RatingSystem.controller.UserController;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.db.repository.UserRepository;
import com.leverx.training.RatingSystem.enums.Role;
import com.leverx.training.RatingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;

@SpringBootApplication
public class RatingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingSystemApplication.class, args);
	}

}
