
package ecommerce_backend.service;

import ecommerce_backend.dto.CategoryRequest;
import ecommerce_backend.dto.CategoryResponse;
import ecommerce_backend.entity.Category;
import ecommerce_backend.exception.ResourceNotFoundException;
import ecommerce_backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new IllegalArgumentException(
                    "Category already exists: " + request.getName().trim());
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return toResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        return toResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

    Category category = categoryRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Category not found with id: " + id));

    String newName = request.getName().trim();

    boolean duplicateName = categoryRepository
            .findByNameIgnoreCase(newName)
            .filter(existing -> !existing.getId().equals(id))
            .isPresent();

    if (duplicateName) {
        throw new IllegalArgumentException(
                "Category already exists: " + newName);
    }

    category.setName(newName);
    category.setDescription(request.getDescription());

    Category updatedCategory = categoryRepository.save(category);

    return toResponse(updatedCategory);
}

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getActive()
        );
    }
}
