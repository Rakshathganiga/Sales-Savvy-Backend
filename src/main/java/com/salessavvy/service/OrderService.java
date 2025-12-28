package com.salessavvy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessavvy.entities.OrderItem;
import com.salessavvy.entities.Product;
import com.salessavvy.entities.ProductImage;
import com.salessavvy.entities.User;
import com.salessavvy.repository.OrderItemRepository;
import com.salessavvy.repository.ProductImageRepository;
import com.salessavvy.repository.ProductRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	ProductImageRepository productImageRepository;
	
	@Autowired
	ProductRepository productRepository;

	public Map<String, Object> getOrdersForUser(User user) {
		
		List<OrderItem> orderItems = orderItemRepository.findSuccessfullOrderItemsByUserId(user.getUserId());
		Map<String, Object> response = new HashMap<>();
		response.put("username", user.getUsername());
		response.put("role", user.getRole());
		
		List<Map<String, Object>> products = new ArrayList<>();
		
		for(OrderItem item : orderItems) {
			Product product = productRepository.findById(item.getProductId()).orElse(null);
			if(product == null) {
				continue;
			}
			
			List<ProductImage> images = productImageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();
			
			Map<String, Object> productDetails = new HashMap<>();
			productDetails.put("order_id", item.getOrder().getOrderId());
			productDetails.put("quantity", item.getQuantity());
			productDetails.put("total_price", item.getTotalPrice());
			productDetails.put("image_url", imageUrl);
			productDetails.put("product_id", product.getProductId());
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", item.getPricePerUnit());
			
			products.add(productDetails);
		}
		
		response.put("products", products);
		
		return response;
	}
	
	
}
