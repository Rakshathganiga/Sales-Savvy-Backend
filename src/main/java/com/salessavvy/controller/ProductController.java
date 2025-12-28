package com.salessavvy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salessavvy.entities.Product;
import com.salessavvy.entities.User;
import com.salessavvy.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String category, HttpServletRequest request) {
		try {
			User authenticatedUser = (User) request.getAttribute("authenticatedUser");
			if(authenticatedUser == null) {
				return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
			}
			
			List<Product> products = productService.getProductsByCategory(category);
			
			Map<String, Object> response = new HashMap<>();
			
			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("name", authenticatedUser.getUsername());
			userInfo.put("role", authenticatedUser.getRole().name());
			
			response.put("user", userInfo);
			
			List<Map<String, Object>> productList = new ArrayList<>();
			
			for(Product product : products) {
				Map<String, Object> productDetails = new HashMap<>();
				productDetails.put("product_id", product.getProductId());
				productDetails.put("name", product.getName());
				productDetails.put("description", product.getDescription());
				productDetails.put("price", product.getPrice());
				productDetails.put("stock", product.getStock());
				
				List<String> images = productService.getProductImages(product.getProductId());
				productDetails.put("images", images);
				productList.add(productDetails);
			}
			response.put("products", productList);
			return ResponseEntity.ok(response);
		}
		catch(RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}






