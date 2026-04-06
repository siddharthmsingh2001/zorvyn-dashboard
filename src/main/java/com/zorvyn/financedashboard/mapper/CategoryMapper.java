package com.zorvyn.financedashboard.mapper;

import com.zorvyn.financedashboard.dtos.CategoryDto;
import com.zorvyn.financedashboard.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> categories);
}
