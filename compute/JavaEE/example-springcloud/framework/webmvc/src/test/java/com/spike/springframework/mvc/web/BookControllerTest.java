package com.spike.springframework.mvc.web;

import com.spike.springframework.mvc.domain.exception.BookNotFoundException;
import com.spike.springframework.mvc.domain.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BookController.class) // Controller测试
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc; // 在mock环境中测试controller

    //    @MockBean // 已废弃
    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "73737313940";

        // Mockito行为
        BDDMockito.given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mockMvc
                // 执行请求
                .perform(MockMvcRequestBuilders.get("/books/" + isbn))
                // 预期响应
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
