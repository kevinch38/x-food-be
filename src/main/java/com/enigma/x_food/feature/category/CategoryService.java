package com.enigma.x_food.feature.category;

import com.enigma.x_food.constant.ECategory;
import com.enigma.x_food.feature.category.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    Category getByCategoryName(ECategory name);
    List<CategoryResponse> getAll();
}
