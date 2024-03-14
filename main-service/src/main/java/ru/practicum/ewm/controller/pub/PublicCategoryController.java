package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CategoryDto;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(required = false, defaultValue = "0") int from,
                                     @RequestParam(required = false, defaultValue = "10") int size) {

        return categoryMapper.toDto(categoryService.findAll(from, size));
    }

    @GetMapping("{catId}")
    public CategoryDto findById(@PathVariable Long catId) {
        return categoryMapper.toDto(categoryService.findById(catId));
    }

}
