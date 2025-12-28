package com.salessavvy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessavvy.entities.User;
import com.salessavvy.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity.ok(Map.of("message", "User Registered Successfully", "user", registeredUser));
		}
		catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	
}
