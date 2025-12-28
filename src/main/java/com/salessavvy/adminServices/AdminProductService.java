package com.salessavvy.adminServices;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.salessavvy.entities.Category;
import com.salessavvy.entities.Product;
import com.salessavvy.entities.ProductImage;
import com.salessavvy.repository.CategoryRepository;
import com.salessavvy.repository.ProductImageRepository;
import com.salessavvy.repository.ProductRepository;


@Service
public class AdminProductService {

	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final CategoryRepository categoryRepository;
	
	public AdminProductService(ProductRepository productRepository, ProductImageRepository productImageRepository,
			CategoryRepository categoryRepository) {
		super();
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.categoryRepository = categoryRepository;
	}

	
	public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId,
			String imageUrl) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		
		if(category.isEmpty()) {
			throw new IllegalArgumentException("Invalid Category ID");
		}
		
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(BigDecimal.valueOf(price));
		product.setStock(stock);
		product.setCategory(category.get());
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());
		
		Product savedProduct = productRepository.save(product);
		
		if(imageUrl != null && !imageUrl.isEmpty()) {
			ProductImage productImage = new ProductImage();
			productImage.setProduct(savedProduct);
			productImage.setImageUrl(imageUrl);
			
			productImageRepository.save(productImage);
		}
		else {
			throw new IllegalArgumentException("Product Image URL cannot be Empty");
		}
		
		return savedProduct;
	}

	
	public void deleteProduct(Integer productId) {
	
		if(!productRepository.existsById(productId)) {
			throw new IllegalArgumentException("Product Not Found");
		}
		
		productImageRepository.deleteById(productId);
		
		productRepository.deleteById(productId);
		
	}

}
