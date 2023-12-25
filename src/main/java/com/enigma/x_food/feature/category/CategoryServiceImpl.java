package com.enigma.x_food.feature.category;

import com.enigma.x_food.constant.ECategory;
import com.enigma.x_food.feature.category.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        List<Category> category = new ArrayList<>();
        for (ECategory value : ECategory.values()) {
            Optional<Category> name = categoryRepository.findByCategoryName(value);
            if (name.isPresent()) continue;

            category.add(Category.builder()
                    .categoryName(value)
                    .build());
        }
        categoryRepository.saveAllAndFlush(category);
    }

    @Transactional(readOnly = true)
    @Override
    public Category getByCategoryName(ECategory name) {
        return findByStatusOrThrowNotFound(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        log.info("Start getAll");

        List<Category> category = categoryRepository.findAll();
        log.info("End getAll");
        return category.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private Category findByStatusOrThrowNotFound(ECategory name) {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category name not found"));
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .categoryID(category.getCategoryID())
                .categoryName(category.getCategoryName().toString())
                .build();
    }
}
