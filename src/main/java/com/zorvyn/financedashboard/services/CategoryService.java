package com.zorvyn.financedashboard.services;

import com.zorvyn.financedashboard.dtos.CategoryDto;
import com.zorvyn.financedashboard.entities.User;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto, User user);

    void deleteCategory(Integer id, User user);

    List<CategoryDto> getMyCategories(User user);

}
