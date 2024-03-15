package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CategoryDto;
import ru.practicum.ewm.controller.dto.NewCategoryDto;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryMapper.toDto(categoryService.create(newCategoryDto));
    }

    @PatchMapping("{catId}")
    CategoryDto update(@PathVariable Long catId,
                       @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryMapper.toDto(categoryService.update(catId, newCategoryDto));
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long catId) {
        categoryService.delete(catId);
    }
}
