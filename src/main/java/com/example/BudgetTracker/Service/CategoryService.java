package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.Entity.Category;
import com.example.BudgetTracker.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.deleteById(categoryId);
    }

    public List<Category> getCategoriesByUserId(UUID id) {
        List<Category> categoryList = categoryRepository.findByUserId(id);
        if (categoryList.isEmpty()) {
            throw new RuntimeException("No categories from user");
        }
        return categoryList;
    }
}