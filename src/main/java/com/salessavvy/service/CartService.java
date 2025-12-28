package com.salessavvy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessavvy.entities.CartItem;
import com.salessavvy.entities.Product;
import com.salessavvy.entities.ProductImage;
import com.salessavvy.entities.User;
import com.salessavvy.repository.CartRepository;
import com.salessavvy.repository.ProductImageRepository;
import com.salessavvy.repository.ProductRepository;
import com.salessavvy.repository.UserRepository;

@Service
public class CartService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ProductImageRepository productImageRepository;

	public void addToCart(Integer userId, int productId, int quantity) {
		
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found with ID : " + userId));
		Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found with ID : " + productId));
		
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartRepository.save(cartItem);
		}
		else {
			CartItem newItem = new CartItem(user, product, quantity);
			cartRepository.save(newItem);
		}
	}
	
	
	public int getCartItemCount(int userId) {
		return cartRepository.getTotalCount(userId);
	}
	
	
	
	public Map<String, Object> getCartItems(int userId) {
		List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);
		
		Map<String, Object> response = new HashMap<>();
		
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
		response.put("username", user.getUsername());
		response.put("role", user.getRole().toString());
		
		List<Map<String, Object>> products = new ArrayList<>();
		int overallTotalPrice = 0;
		
		for(CartItem cartItem : cartItems) {
			Map<String, Object> productDetails = new HashMap<>();
			Product product = cartItem.getProduct();
			
			List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = (productImages != null && !productImages.isEmpty()) ? productImages.get(0).getImageUrl() : "default-image-url";
			
			productDetails.put("product_id", product.getProductId());
			productDetails.put("image_url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());
			
			products.add(productDetails);
			overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
		}
		
		Map<String, Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);
		
		response.put("cart", cart);
		
		return response;
	}
	
	public void updateCartItemQuantity(int userId, int productId, int quantity) {
		
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
		
		Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
		
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(user.getUserId(), product.getProductId());
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			if(quantity == 0) {
				deleteCartItem(userId, productId);
			}
			else {
				cartItem.setQuantity(quantity);
				cartRepository.save(cartItem);
			}
		}
		
	}


	public void deleteCartItem(int userId, int productId) {
		cartRepository.deleteCartItem(userId, productId);
	}
	
	
	
	
	
}
