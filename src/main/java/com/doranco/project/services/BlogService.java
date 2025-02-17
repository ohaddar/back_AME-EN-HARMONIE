package com.doranco.project.services;

import com.doranco.project.entities.Blog;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Service
public interface BlogService {
    Blog saveBlog(String blogJson, MultipartFile file);
    Optional<Blog> getBlogById(Long id);
    List<Blog> getAllBlogs() ;
    void deleteBlogById(Long id);
    Blog updateBlogById(Long id, String blogJson, MultipartFile file);
    List<Blog> getBlogsByCategory(String category);
}
