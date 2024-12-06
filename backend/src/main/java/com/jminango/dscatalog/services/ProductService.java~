package com.jminango.dscatalog.services;

import com.jminango.dscatalog.dto.CategoryDto;
import com.jminango.dscatalog.dto.ProductDto;
import com.jminango.dscatalog.entities.Category;
import com.jminango.dscatalog.entities.Product;
import com.jminango.dscatalog.repositories.CategoryRepository;
import com.jminango.dscatalog.repositories.ProductRepository;
import com.jminango.dscatalog.services.exceptions.DataBaseException;
import com.jminango.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaginated(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(x -> new ProductDto(x));
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDto(entity, entity.getCategories());
    }

    @Transactional
    public ProductDto insert(ProductDto productDto) {
        Product entity = new Product();
        productDTOtoEntity(productDto, entity);
        entity = repository.save(entity);
        return new ProductDto(entity);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto productDto) {
        try {
            Product entity = repository.getOne(id);
            productDTOtoEntity(productDto, entity);
            entity = repository.save(entity);
            return new ProductDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void productDTOtoEntity(ProductDto productDto, Product entity) {
        entity.setName(productDto.getName());
        entity.setDescription(productDto.getDescription());
        entity.setPrice(productDto.getPrice());
        entity.setImgUrl(productDto.getImgUrl());
        entity.setDate(productDto.getDate());

        entity.getCategories().clear();

        for (CategoryDto catDto : productDto.getCategories()) {
             Category category = categoryRepository.getOne(catDto.getId());
             category.setName(catDto.getName());
             entity.getCategories().add(category);
        }
    }
}
