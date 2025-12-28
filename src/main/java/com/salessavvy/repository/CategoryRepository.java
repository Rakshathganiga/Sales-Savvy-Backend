package com.salessavvy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salessavvy.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findByCategoryName(String categoryName);
	
}
