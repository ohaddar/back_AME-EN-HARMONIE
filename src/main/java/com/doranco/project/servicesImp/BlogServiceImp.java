package com.doranco.project.servicesImp;

import com.doranco.project.entities.Blog;
import com.doranco.project.repositories.IBlogRepository;
import com.doranco.project.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class BlogServiceImp implements BlogService {
    @Autowired
    IBlogRepository blogRepository;
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);

    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);

    }

    @Override
    public List<Blog> getBlog() {
        return blogRepository.findAll();

    }

    @Override
    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public void deleteBlog(Blog blog) {
        blogRepository.delete(blog);
    }


    @Override
    public Blog updateBlogById(Long id, Blog updatedBlog) {

        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog existingBlog = optionalBlog.get();
            existingBlog.setTitle(updatedBlog.getTitle());
            existingBlog.setContent(updatedBlog.getContent());
            existingBlog.setCategory(updatedBlog.getCategory());
            existingBlog.setCreationDate(updatedBlog.getCreationDate());
            return blogRepository.save(existingBlog);
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }

    @Override
    public List<Blog> getBlogsByTopic(String title) {
        return blogRepository.findByTitleContainingIgnoreCase(title);

    }

    @Override
    public List<Blog> getBlogsByCategory(String category) {
        return blogRepository.findByCategoryContainingIgnoreCase(category);
    }
}
