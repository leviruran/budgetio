package com.wallet_web_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet_web_app.dto.CategoryDTO;
import com.wallet_web_app.dto.SubcategoryDTO;
import com.wallet_web_app.entity.Category;
import com.wallet_web_app.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.wallet_web_app.entity.Subcategory;
import com.wallet_web_app.repository.SubcategoryRepository;



@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        List<Subcategory> subcategories = categoryDTO.getSubcategories().stream()
                .map(subcatDTO -> {
                    Subcategory subcategory = new Subcategory();
                    subcategory.setName(subcatDTO.getName());
                    subcategory.setCategory(category);
                    return subcategory;
                })
                .collect(Collectors.toList());

        category.setSubcategories(subcategories);

        categoryRepository.save(category);
        subcategoryRepository.saveAll(subcategories); 

        List<SubcategoryDTO> subcategoryDTOs = subcategories.stream()
                .map(subcat -> new SubcategoryDTO(subcat.getId(), subcat.getName()))
                .collect(Collectors.toList());

        return new CategoryDTO(category.getId(), category.getName(), subcategoryDTOs);
    }


    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(cat -> {
                    List<SubcategoryDTO> subcategoryDTOs = cat.getSubcategories().stream()
                            .map(subcat -> new SubcategoryDTO(subcat.getId(), subcat.getName()))
                            .collect(Collectors.toList());
                    return new CategoryDTO(cat.getId(), cat.getName(), subcategoryDTOs);
                })
                .collect(Collectors.toList());
    }
}
