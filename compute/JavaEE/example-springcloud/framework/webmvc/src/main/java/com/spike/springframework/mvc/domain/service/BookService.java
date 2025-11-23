package com.spike.springframework.mvc.domain.service;

import com.spike.springframework.mvc.domain.exception.BookAlreadyExistsException;
import com.spike.springframework.mvc.domain.exception.BookNotFoundException;
import com.spike.springframework.mvc.domain.model.Book;
import com.spike.springframework.mvc.infra.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                // update
                .map(existingBook -> {
                    var bookToUpdate = new Book(
                            existingBook.id(), // 主键
                            existingBook.isbn(),
                            book.title(),
                            book.author(),
                            book.price(),
                            existingBook.version() // 版本
                    );
                    return bookRepository.save(bookToUpdate);
                })
                // insert
                .orElseGet(() -> addBookToCatalog(book));
    }
}
