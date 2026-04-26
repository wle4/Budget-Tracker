package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.CategoryRequestDTO;
import com.example.BudgetTracker.DTO.CategoryResponseDTO;
import com.example.BudgetTracker.Entity.Category;
import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.CategoryRepository;
import com.example.BudgetTracker.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void createCategory_ValidUser_ReturnsCategoryResponseDTO() {
        // arrange
        CategoryRequestDTO dto = new CategoryRequestDTO();
        dto.setName("TestName");

        User user = new User();
        user.setEmail("example@gmail.com");

        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("TestName");
        category.setUser(user);

        // tell
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        // act
        CategoryResponseDTO result = categoryService.createCategory(dto, user.getEmail());

        // assert
        assertNotNull(result);
        assertEquals("TestName", result.getName());
        assertEquals(user.getId(), result.getUserId());

        // verify
    }

    @Test
    void createCategory_InvalidUser_ThrowsException() {
        // arrange
        String invalidEmail = "invalidEmail@gmail.com";

        CategoryRequestDTO dto = new CategoryRequestDTO();
        dto.setName("TestName");

        // tell
        when(userRepository.findByEmail(invalidEmail))
                .thenReturn(Optional.empty());

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(dto, invalidEmail);
        });

        // assert
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getCategoriesByUserId_ValidUser_ReturnsCategoryResponseDTOList() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Category category = new Category();
        category.setUser(user);
        category.setName("TestName");
        category.setId(UUID.randomUUID());

        List<Category> categoryList = List.of(category);

        // tell
        when(categoryRepository.findByUserId(user.getId()))
                .thenReturn(categoryList);

        // act
        List<CategoryResponseDTO> result = categoryService.getCategoriesByUserId(user.getId());

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
        assertEquals(category.getId(), result.get(0).getId());
    }

    @Test
    void getCategoriesByUserId_NoCategories_ThrowsException() {
        // arrange
        User user = new User();
        user.setId(UUID.randomUUID());

        // tell
        when(categoryRepository.findByUserId(user.getId()))
                .thenReturn(List.of());

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoriesByUserId(user.getId());
        });

        // assert
        assertEquals("No categories associated with this user", exception.getMessage());
    }

    @Test
    void deleteCategory_ValidCategoryId_DeletesCategory() {
        // arrange
        Category category = new Category();
        category.setId(UUID.randomUUID());

        // tell
        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        // act
        categoryService.deleteCategory(category.getId());

        // verify
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    void deleteCategory_InvalidCategoryId_ThrowsException() {
        // arrange
        UUID invalidId = UUID.randomUUID();

        // act tell
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(invalidId);
        });

        // assert
        assertEquals("Category not found", exception.getMessage());
    }
}
