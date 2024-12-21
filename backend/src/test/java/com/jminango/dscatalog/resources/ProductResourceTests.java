package com.jminango.dscatalog.resources;


import com.jminango.dscatalog.dto.ProductDto;
import com.jminango.dscatalog.services.ProductService;
import com.jminango.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(controllers = ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private ProductDto productDto;
    private PageImpl<ProductDto> page;

    @BeforeEach
    void setUp() {

        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDto));

        when(service.findAllPaginated(any())).thenReturn(page);
    }

    @Test
    public void findAllShouldReturnPagedResponse() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }


}
