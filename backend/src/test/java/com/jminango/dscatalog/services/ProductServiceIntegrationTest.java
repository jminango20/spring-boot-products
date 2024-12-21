package com.jminango.dscatalog.services;

import com.jminango.dscatalog.dto.ProductDto;
import com.jminango.dscatalog.repositories.ProductRepository;
import com.jminango.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistentId;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp() {
        existingId = 1L;
        nonExistentId = 1000L;

        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistentId);
        });
    }

    @Test
    public void findAllPaginatedShouldReturnPageWhenPageNumberIsZeroAndPageSizeIsTen() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductDto> result = service.findAllPaginated(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPaginatedShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageable = PageRequest.of(50, 10);
        Page<ProductDto> result = service.findAllPaginated(pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPaginatedSortByNameShouldReturnSortedPage() {

        PageRequest pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDto> result = service.findAllPaginated(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
