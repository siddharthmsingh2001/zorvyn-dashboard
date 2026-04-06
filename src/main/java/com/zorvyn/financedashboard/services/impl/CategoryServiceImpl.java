package com.zorvyn.financedashboard.services.impl;

import com.zorvyn.financedashboard.dtos.CategoryDto;
import com.zorvyn.financedashboard.entities.Category;
import com.zorvyn.financedashboard.entities.User;
import com.zorvyn.financedashboard.exception.ResponseStatus;
import com.zorvyn.financedashboard.exception.custom.BadRequestException;
import com.zorvyn.financedashboard.exception.custom.ResourceAlreadyExistsException;
import com.zorvyn.financedashboard.exception.custom.ResourceNotFoundException;
import com.zorvyn.financedashboard.mapper.CategoryMapper;
import com.zorvyn.financedashboard.repositories.CategoryRepository;
import com.zorvyn.financedashboard.repositories.FinanceRecordRepository;
import com.zorvyn.financedashboard.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FinanceRecordRepository financeRecordRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto, User user) {
        // Ensure that the Category does not already exist for the user;
        if (categoryRepository.existsByNameAndUserAndIsDeletedFalse(categoryDto.name(), user)) {
            throw new ResourceAlreadyExistsException("A category with this name already exists.", ResponseStatus.CATEGORY_EXISTS);
        }
        // Persist the Category
        Category category = new Category();
        category.setName(categoryDto.name());
        category.setUser(user);
        category.setParent(null);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id, User user) {
        // Check if Category exists
        Category categoryToDelete = categoryRepository.findByIdAndUserAndIsDeletedFalse(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found", ResponseStatus.CATEGORY_NOT_FOUND));
        // Make sure user does not delete the fallback Category 'Miscellaneous'
        if (categoryToDelete.getName().equalsIgnoreCase("MISCELLANEOUS")) {
            throw new BadRequestException("The 'MISCELLANEOUS' category cannot be deleted.", ResponseStatus.BAD_REQUEST);
        }
        // Get the 'Miscellaneous' category
        Category miscCategory = categoryRepository.findByNameAndUserAndIsDeletedFalse("MISCELLANEOUS", user).orElseGet(() -> {
                    Category newMisc = new Category();
                    newMisc.setName("MISCELLANEOUS");
                    newMisc.setUser(user);
                    newMisc.setParent(null);
                    return categoryRepository.save(newMisc);
                });
        // Associate all the records related to the 'categoryToDelete' category to the 'Miscellaneous'.
        financeRecordRepository.updateCategoryForUser(categoryToDelete, miscCategory, user);
        // Set the delete status
        categoryToDelete.setDeleted(true);
        categoryRepository.save(categoryToDelete);
    }

    @Override
    @Transactional
    public List<CategoryDto> getMyCategories(User user) {
        // Retrieve all the Non Deleted Category associated with the user.
        List<Category> categories = categoryRepository.findByUserAndIsDeletedFalse(user);
        return categoryMapper.toDtoList(categories);
    }
}
