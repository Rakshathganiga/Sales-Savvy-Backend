package com.salessavvy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessavvy.entities.Category;
import com.salessavvy.entities.Product;
import com.salessavvy.entities.ProductImage;
import com.salessavvy.repository.CategoryRepository;
import com.salessavvy.repository.ProductImageRepository;
import com.salessavvy.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return productRepository.findByCategory_CategoryId(category.getCategoryId());
			}
			else {
				throw new RuntimeException("Category Not Found");
			}
		}
		else {
			return productRepository.findAll();
		}
	}
	
	public List<String> getProductImages(Integer productId) {
		List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(productId);
		List<String> imageUrls = new ArrayList<String>();
		for(ProductImage image : productImages) {
			imageUrls.add(image.getImageUrl());
		}
		return imageUrls;
	}
}
