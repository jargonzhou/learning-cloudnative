package com.spike.springframework.mvc.infra.repository;

import com.spike.springframework.mvc.domain.model.Book;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
//    Iterable<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

//    Book save(Book book);

    @Transactional // 开启事务
    @Modifying // 修改操作
    @Query("delete from Book where isbn = :isbn")
    void deleteByIsbn(String isbn);

// 内存中实现
//    @Repository
//    class ImMemoryBookRepository implements BookRepository {
//        private static final Map<String, Book> books = new ConcurrentHashMap<>();
//
//        @Override
//        public Iterable<Book> findAll() {
//            return books.values();
//        }
//
//        @Override
//        public Optional<Book> findByIsbn(String isbn) {
//            return this.existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
//        }
//
//        @Override
//        public boolean existsByIsbn(String isbn) {
//            return books.containsKey(isbn);
//        }
//
//        @Override
//        public Book save(Book book) {
//            books.put(book.isbn(), book);
//            return book;
//        }
//
//        @Override
//        public void deleteByIsbn(String isbn) {
//            books.remove(isbn);
//        }
//    }
}
