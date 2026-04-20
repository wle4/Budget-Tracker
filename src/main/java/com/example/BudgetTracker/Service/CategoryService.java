package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.CategoryRequestDTO;
import com.example.BudgetTracker.DTO.CategoryResponseDTO;
import com.example.BudgetTracker.Entity.Category;
import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.CategoryRepository;
import com.example.BudgetTracker.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Category category = new Category();
        category.setUser(user);
        category.setName(dto.getName());

        Category saved = categoryRepository.save(category);
        return toResponseDTO(saved);
    }
//    public Category createCategory(Category category) {
//        return categoryRepository.save(category);
//    }

    public List<CategoryResponseDTO> getCategoriesByUserId(UUID userId) {
        List<Category> categoryList = categoryRepository.findByUserId(userId);

        if (categoryList.isEmpty()) {
            throw new RuntimeException("No categories associated with this user");
        }

        return categoryList.stream().map(this::toResponseDTO).toList();
    }

//    public List<Category> getCategoriesByUserId(UUID id) {
//        List<Category> categoryList = categoryRepository.findByUserId(id);
//        if (categoryList.isEmpty()) {
//            throw new RuntimeException("No categories from user");
//        }
//        return categoryList;
//    }

    public void deleteCategory(UUID categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.deleteById(categoryId);
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();

        dto.setId(category.getId());
        dto.setUserId(category.getUser().getId());
        dto.setName(category.getName());

        return dto;
    }
}