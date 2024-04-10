package com.spike.spring.catalog.web;

import com.spike.spring.catalog.domain.Book;
import com.spike.spring.catalog.domain.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> get() {
        return bookService.viewBookList();
    }

    @GetMapping("/{isbn}")
    public Book getByIsbn(@PathVariable(name = "isbn") String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    // authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnSThEZXh0cTFxaTF5THNteFVfM1pkWGw2d0pWejA3TmhJQXl4bTViRVBNIn0.eyJleHAiOjE3MTI0NzM0NzAsImlhdCI6MTcxMjQ3MzE3MCwiYXV0aF90aW1lIjoxNzEyNDcxNTkwLCJqdGkiOiI4YmVjNjc2Yy04ZDA1LTQ4NzgtYTVkYi00NWExMmNjZGY5MTEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjE4MDgwL3JlYWxtcy9ib29rc3RvcmUiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOGRjMzk0OTUtZGMzMC00N2Q2LTljMmQtODJiYjU4NDEzODJkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZWRnZS1zZXJ2aWNlIiwibm9uY2UiOiJxckpLamtHR1VLNmtNY3BEZGxob0VObTJIZndBVjZibmd2aTk4bGU4b1NvIiwic2Vzc2lvbl9zdGF0ZSI6ImFiMWYwOWUyLWRjYTMtNGZkZi1hYWZiLTdmMDFjMjYyOTFhMSIsImFjciI6IjAiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovLzE5Mi4xNjguMy4xNzg6OTAwMCJdLCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6ImFiMWYwOWUyLWRjYTMtNGZkZi1hYWZiLTdmMDFjMjYyOTFhMSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1ib29rc3RvcmUiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZW1wbG95ZWUiLCJjdXN0b21lciJdLCJuYW1lIjoiSXNhYmVsbGUgRGFobCIsInByZWZlcnJlZF91c2VybmFtZSI6ImlzYWJlbGxlIiwiZ2l2ZW5fbmFtZSI6IklzYWJlbGxlIiwiZmFtaWx5X25hbWUiOiJEYWhsIn0.VGh3gZxkIjQI_81j7D1PznwlgRVnYBsponhFWAVXFxVj1JUQujzg9_liSzemudbstwSsRwfGRnro9KvXbl_H8RJ_xZwkXnFdbyHAtg5RJni4AYKVXiDeMIJuyVq60CsbzgNgkJP9JdOqyt--dP0MM8__gaBycwibGfv4soq_j8pO729HCaK8AIOWGwUJBervAELI40RIjTxIr4nnIjzmbxszN8ul3yKDZ2EpaN-mgSrbHd7vbMcyXLV1VNuER_Vo6hKvgM9CDjk9naq3aJ8O-EPW8jo7lzYSznzOd4rFKCDiVxh968pTWFp4wl_Zn4YSsTT_PHpAazDPMajm4NdE-g
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody Book book) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // see JwtAuthenticationToken
        log.debug("User: {}", jwt.getClaimAsString("preferred_username"));
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("/{isbn}")
    public void delete(@PathVariable(name = "isbn") String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("/{isbn}")
    public Book put(@PathVariable(name = "isbn") String isbn, @Valid @RequestBody Book book) {
        return bookService.editBookDetails(isbn, book);
    }

}
