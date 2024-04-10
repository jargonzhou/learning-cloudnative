package com.spike.spring.catalog.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public interface BookRepository extends CrudRepository<Book, Long> {
//    List<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

//    Book save(Book book);

    @Modifying
    @Query("DELETE FROM book WHERE isbn = :isbn")
    @Transactional
    void deleteByIsbn(String isbn);
}
