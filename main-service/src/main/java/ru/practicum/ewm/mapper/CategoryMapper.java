package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.controller.dto.CategoryDto;
import ru.practicum.ewm.controller.dto.NewCategoryDto;
import ru.practicum.ewm.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);
    CategoryDto toDto(Category category);
    List<CategoryDto> toDto(List<Category> categories);
}
