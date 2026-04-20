package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.DTO.CategoryRequestDTO;
import com.example.BudgetTracker.DTO.CategoryResponseDTO;
import com.example.BudgetTracker.Entity.Category;
import com.example.BudgetTracker.Service.CategoryService;
import com.example.BudgetTracker.Service.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final JwtService jwtService;

    public CategoryController(CategoryService categoryService, JwtService jwtService) {
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CategoryRequestDTO dto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        return categoryService.createCategory(dto, email);
    }

    @GetMapping
    public List<CategoryResponseDTO> getCategories(@RequestParam UUID userId) {
        return categoryService.getCategoriesByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }
}