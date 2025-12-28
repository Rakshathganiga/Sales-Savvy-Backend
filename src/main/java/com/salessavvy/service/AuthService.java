package com.salessavvy.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.salessavvy.entities.JWTToken;
import com.salessavvy.entities.User;
import com.salessavvy.repository.JWTTokenRepository;
import com.salessavvy.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

	private final Key SIGNING_KEY;
	
	private final UserRepository userRepo;
	private final JWTTokenRepository jwtTokenRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository userRepo, JWTTokenRepository jwtTokenRepository, 
			@Value("${jwt.secret}") String jwtSecret) {
		super();
		this.userRepo = userRepo;
		this.jwtTokenRepository = jwtTokenRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
		if(jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
			throw new IllegalArgumentException("JWT_SECRET in application.properties must be atleast 64 bytes long for HS512.");
		}
		
		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	public User authenticate(String username, String password) {
		
		User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid Username"));
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid Password");
		}
		
		return user;
	}
	
	public String generateToken(User user) {
		String token;
		LocalDateTime now = LocalDateTime.now();
		JWTToken existingToken = jwtTokenRepository.findByUserId(user.getUserId());
		
		if(existingToken != null && now.isBefore(existingToken.getExpiresAt())) {
			token = existingToken.getToken();
		}
		else {
			token = generateNewToken(user);
			if(existingToken != null) {
				jwtTokenRepository.delete(existingToken);
			}
			saveToken(user, token);
		}
		return token;
	}
	
	public String generateNewToken(User user) {
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("role", user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
				.compact();
	}
	
	public void saveToken(User user, String token) {
		JWTToken jwtToken = new JWTToken(user, token, LocalDateTime.now().plusHours(1));
		jwtTokenRepository.save(jwtToken);
	}

	public boolean validateToken(String token) {
		try {
			System.err.println("Validating Token...");
			
			Jwts.parser()
				.setSigningKey(SIGNING_KEY)
				.build()
				.parseClaimsJws(token);
			
			Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);
			if(jwtToken.isPresent()) {
				System.err.println("Token Expiry: " + jwtToken.get().getExpiresAt());
				System.err.println("Current time: " + LocalDateTime.now());
				return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
			}
			return false;
		}
		catch(Exception e) {
			System.out.println("Token validation failed: " + e.getMessage());
			return false;
		}
	}

	public String extractUsername(String token) {
		return Jwts.parser()
				.setSigningKey(SIGNING_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public void logout(User user) {
		int userId = user.getUserId();
		JWTToken token = jwtTokenRepository.findByUserId(userId);
		
		if(token != null) {
			jwtTokenRepository.deleteByUserId(userId);
		}
	}
	
	
}
