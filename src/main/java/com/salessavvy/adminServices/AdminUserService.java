package com.salessavvy.adminServices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salessavvy.entities.Role;
import com.salessavvy.entities.User;
import com.salessavvy.repository.JWTTokenRepository;
import com.salessavvy.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminUserService {
	
	private final UserRepository userRepository;
	private final JWTTokenRepository jwtTokenRepository;
	
	public AdminUserService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository) {
		super();
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
	}

	@Transactional
	public User modifyUser(Integer userId, String username, String email, String role) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isEmpty()) {
			throw new IllegalArgumentException("User Not Found");
		}
		
		User existingUser = userOptional.get();
		
		if(username != null && !username.isEmpty()) {
			existingUser.setUsername(username);
		}
		
		if(email != null && !email.isEmpty()) {
			existingUser.setEmail(email);
		}
		
		if(role != null && !role.isEmpty()) {
			try {
				existingUser.setRole(Role.valueOf(role));
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid Role: " + role);
			}
		}
		
		jwtTokenRepository.deleteByUserId(userId);
		
		return userRepository.save(existingUser);
	}

	
	public User getUserById(Integer userId) {

		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
	}
}
