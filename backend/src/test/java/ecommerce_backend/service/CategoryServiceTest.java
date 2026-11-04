package ecommerce_backend.service;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import ecommerce_backend.dto.CategoryRequest;
import ecommerce_backend.dto.CategoryResponse;
import ecommerce_backend.entity.Category;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    private Category category;

    @InjectMocks
    private CategoryService categoryService;  

     @BeforeEach
        void setUp() {
            category = new Category();
            category.setName("Electronics");
            category.setDescription("Electronic products");
        }



    @Test
        void createCategoryShouldSaveCategorySuccessfully() {
            CategoryRequest request = new CategoryRequest();
            request.setName("Electronics");
            request.setDescription("Electronic products");

            when(categoryRepository.existsByNameIgnoreCase("Electronics"))
            .thenReturn(false);

            when(categoryRepository.save(any(Category.class)))
            .thenReturn(category);
            CategoryResponse response = categoryService.createCategory(request);
            assertEquals(category.getName(), response.getName());
            assertEquals(category.getDescription(), response.getDescription());
            assertNotNull(response);
            verify(categoryRepository)
                    .existsByNameIgnoreCase("Electronics");

            verify(categoryRepository)
                    .save(any(Category.class));
        }


        @Test 
        void createCategoryShouldThrowExceptionWhenCategoryAlreadyExists() {
            CategoryRequest request = new CategoryRequest();
            request.setName("Electronics");
            request.setDescription("Electronic products");

            when(categoryRepository.existsByNameIgnoreCase("Electronics"))
            .thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                categoryService.createCategory(request);
            });

            assertEquals("Category already exists: Electronics", exception.getMessage());
            verify(categoryRepository)
                    .existsByNameIgnoreCase("Electronics");
            verify(categoryRepository, never()).save(any(Category.class));      
        }


        @Test 
            void getAllCategoriesShouldReturnListOfCategories() {
                when(categoryRepository.findAll())
                .thenReturn(List.of(category));

                var response = categoryService.getAllCategories();

                assertNotNull(response);
                assertEquals(1, response.size());
                assertEquals(category.getName(), response.get(0).getName());
                assertEquals(category.getDescription(), response.get(0).getDescription());

                verify(categoryRepository).findAll();
            }
           
         @Test
            void getAllCategoriesShouldReturnEmptyListWhenNoCategoriesExist() {
                when(categoryRepository.findAll())
                .thenReturn(List.of());

                var response = categoryService.getAllCategories();

                assertNotNull(response);
                assertTrue(response.isEmpty());

                verify(categoryRepository).findAll();
            }
            
            
        @Test 
            void getCategoryByIdShouldReturnCategoryWhenExists() {
                when(categoryRepository.findById(1L))
                .thenReturn(java.util.Optional.of(category));

                var response = categoryService.getCategoryById(1L);

                assertNotNull(response);
                assertEquals(category.getName(), response.getName());
                assertEquals(category.getDescription(), response.getDescription());

                verify(categoryRepository).findById(1L);
        }

      
        @Test
        void getCategoryByIdShouldThrowExceptionWhenCategoryDoesNotExist() {

            // Arrange
            when(categoryRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // Act
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> categoryService.getCategoryById(1L)
            );

            // Assert
            assertEquals("Category not found with id: 1", exception.getMessage());
            verify(categoryRepository).findById(1L);
        }

        @Test
        void deleteCategoryShouldDeleteCategoryWhenExists() {

            // Arrange
            when(categoryRepository.findById(1L))
                    .thenReturn(Optional.of(category));

            // Act
            categoryService.deleteCategory(1L);

            // Assert
            verify(categoryRepository).findById(1L);
            verify(categoryRepository).delete(category);
        }

        @Test
        void deleteCategoryShouldThrowExceptionWhenCategoryDoesNotExist() {

            // Arrange
            when(categoryRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // Act
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> categoryService.deleteCategory(1L)
            );

            // Assert
            assertEquals("Category not found with id: 1", exception.getMessage());

            verify(categoryRepository).findById(1L);
            verify(categoryRepository, never()).delete(any(Category.class));
        }
}
