package com.wallet_web_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wallet_web_app.entity.Subcategory;
import com.wallet_web_app.services.SubcategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping("/get/{categoryId}")
    public ResponseEntity<List<Subcategory>> getSubcategoriesByCategory(@PathVariable Long categoryId) {
        List<Subcategory> subcategories = subcategoryService.getSubcategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(subcategories);
    }

    @PostMapping("/add")
    public ResponseEntity<Subcategory> createSubcategory(@RequestBody Subcategory subcategory) {
        Subcategory createdSubcategory = subcategoryService.createSubcategory(subcategory);
        return ResponseEntity.ok(createdSubcategory);
    }
}
