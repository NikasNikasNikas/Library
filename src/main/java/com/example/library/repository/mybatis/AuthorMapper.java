package com.example.library.repository.mybatis;

import com.example.library.entity.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AuthorMapper {

    List<Author> findAllWithBooks();
}