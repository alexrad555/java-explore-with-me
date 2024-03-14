package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.OffsetPageable;
import ru.practicum.ewm.controller.dto.NewCategoryDto;
import ru.practicum.ewm.exception.ConstraintViolationException;
import ru.practicum.ewm.exception.DataNotFoundException;
import ru.practicum.ewm.exception.DuplicateException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    public Category create(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findByName(newCategoryDto.getName()).isPresent()) {
            throw new DuplicateException("email занят");
        }
        Category category = categoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(category);
        return category;
    }


    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new DataNotFoundException(String.format("Категория с id %d не найден", categoryId)));
    }

    public Category update(Long catId, NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new ValidationException("имемя должно быть заполнено");
        }
        Category category = findById(catId);
        category.setName(newCategoryDto.getName());
        categoryRepository.save(category);
        return category;
    }

    public void delete(Long catId) {
        findById(catId);
        if (eventRepository.findFirstByCategoryId(catId).isPresent()) {
            throw new ConstraintViolationException("Существуют события, связанные с категорией");
        }
        categoryRepository.deleteById(catId);
    }

    public List<Category> findAll(int from, int size) {
        Pageable pageable = new OffsetPageable(from, size, Sort.by(Sort.Direction.DESC, "id"));
        return categoryRepository.findAll(pageable).toList();
    }
}
