package com.jminango.dscatalog.tests;

import com.jminango.dscatalog.dto.ProductDto;
import com.jminango.dscatalog.entities.Category;
import com.jminango.dscatalog.entities.Product;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png");
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDto createProductDto() {
        Product product = createProduct();
        return new ProductDto(product, product.getCategories());
    }
}
