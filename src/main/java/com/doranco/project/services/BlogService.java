package com.doranco.project.services;

import com.doranco.project.entities.Blog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BlogService {
    public Blog saveBlog(Blog blog) ;
    public Optional<Blog> getBlogById(Long id);
    public List<Blog> getBlog() ;
    public void deleteBlogById(Long id);
    public void deleteBlog(Blog blog);
    public Blog updateBlogById(Long id, Blog updatedBlog);
    public List<Blog> getBlogsByTopic(String title);
    public List<Blog> getBlogsByCategory(String category);
}
