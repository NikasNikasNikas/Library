package com.example.library.repository.mybatis;

import com.example.library.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BookMapper {
    void insert(Book book);
    List<Book> findAllWithDetails();
    Book findByIdWithDetails(@Param("id") Long id);
}