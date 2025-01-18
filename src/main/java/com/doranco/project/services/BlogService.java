package com.doranco.project.services;

import com.doranco.project.entities.Blog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BlogService {
    Blog saveBlog(Blog blog) ;
    Optional<Blog> getBlogById(Long id);
    List<Blog> getBlog() ;
    void deleteBlogById(Long id);
    Blog updateBlogById(Long id, Blog updatedBlog);
    List<Blog> getBlogsByCategory(String category);
}
