package com.jminango.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jminango.dscatalog.dto.ProductDto;
import com.jminango.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {

        ResultActions result = mockMvc.perform(get("/products?page=0&size=10&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("content").exists());
        result.andExpect(jsonPath("content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        ProductDto productDto = Factory.createProductDto();
        String jsonBody = objectMapper.writeValueAsString(productDto);

        String expectedName = productDto.getName();
        String expectedDescription = productDto.getDescription();

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ProductDto productDto = Factory.createProductDto();
        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistentId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

}
