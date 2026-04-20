package com.example.library.dao;

import com.example.library.entity.Book;
import com.example.library.repository.mybatis.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMyBatisDao {

    private final BookMapper bookMapper;

    public void save(Book book) {
        bookMapper.insert(book);
    }

    public Book findById(Long id) {
        return bookMapper.findByIdWithDetails(id);
    }

}