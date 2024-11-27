package com.jminango.dscatalog.resources;

import com.jminango.dscatalog.dto.CategoryDto;
import com.jminango.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }


}