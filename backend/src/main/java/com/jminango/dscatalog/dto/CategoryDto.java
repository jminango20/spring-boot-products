package com.jminango.dscatalog.dto;

import com.jminango.dscatalog.entities.Category;

public class CategoryDto {

    private Long id;
    private String name;

    public CategoryDto() {
    }

    public CategoryDto(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
