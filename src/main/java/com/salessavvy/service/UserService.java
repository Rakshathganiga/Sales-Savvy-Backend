package com.salessavvy.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.salessavvy.entities.User;
import com.salessavvy.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public User registerUser(User user) {
		
		if(userRepo.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username already taken");
		}
		
		if(userRepo.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already registered");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepo.save(user);
	}
	
}
