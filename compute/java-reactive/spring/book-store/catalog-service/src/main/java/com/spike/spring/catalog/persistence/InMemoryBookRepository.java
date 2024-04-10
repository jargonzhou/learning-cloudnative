//package com.spike.spring.catalog.persistence;
//
//import com.spike.spring.catalog.domain.Book;
//import com.spike.spring.catalog.domain.BookRepository;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public class InMemoryBookRepository implements BookRepository {
//    private static final Map<String, Book> books = new ConcurrentHashMap<>();
//
//    @Override
//    public List<Book> findAll() {
//        return books.values().stream().toList();
//    }
//
//    @Override
//    public Optional<Book> findByIsbn(String isbn) {
//        return this.existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
//    }
//
//    @Override
//    public boolean existsByIsbn(String isbn) {
//        return books.get(isbn) != null;
//    }
//
//    @Override
//    public Book save(Book book) {
//        books.put(book.isbn(), book);
//        return book;
//    }
//
//    @Override
//    public void deleteByIsbn(String isbn) {
//        books.remove(isbn);
//    }
//}
