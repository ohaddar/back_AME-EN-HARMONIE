package com.doranco.project.repositories;

import com.doranco.project.entities.Blog;
import com.doranco.project.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBlogRepository extends MongoRepository<Blog,String> {
    List<Blog> findByTitleContainingIgnoreCase(String title);
    List<Blog> findByCategory(CategoryEnum category);


}