package com.jminango.dscatalog.services;

import com.jminango.dscatalog.dto.ProductDto;
import com.jminango.dscatalog.entities.Product;
import com.jminango.dscatalog.repositories.ProductRepository;
import com.jminango.dscatalog.services.exceptions.DataBaseException;
import com.jminango.dscatalog.services.exceptions.ResourceNotFoundException;
import com.jminango.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistentId;
    private Long dependencyId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    public void setUp() {
        existingId = 1L;
        nonExistentId = 1000L;
        dependencyId = 4L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistentId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistentId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependencyId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistentId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependencyId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependencyId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDto> result = service.findAllPaginated(pageable);

        Assertions.assertNotNull(result);

        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists() {
        ProductDto result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistentId);
        });
        Mockito.verify(repository, Mockito.times(1)).findById(nonExistentId);
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {

        Product product = new Product();
        ProductDto productDto = new ProductDto(product, product.getCategories());
        ProductDto result = service.insert(productDto);
        Assertions.assertNotNull(result.getId());
        Mockito.verify(repository, Mockito.times(1)).save(product);
    }

}
