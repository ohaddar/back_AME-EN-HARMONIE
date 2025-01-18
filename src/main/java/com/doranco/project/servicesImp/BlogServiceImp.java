package com.doranco.project.servicesImp;

import com.doranco.project.entities.Blog;
import com.doranco.project.enums.CategoryEnum;
import com.doranco.project.repositories.IBlogRepository;
import com.doranco.project.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service

public class BlogServiceImp implements BlogService {
    @Autowired
    IBlogRepository blogRepository;
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setCreationDate(new Date());
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
    public Blog updateBlogById(Long id, Blog updatedBlog) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog existingBlog = optionalBlog.get();
            if (updatedBlog.getTitle() != null) {
                existingBlog.setTitle(updatedBlog.getTitle());
            }
            if (updatedBlog.getContent() != null) {
                existingBlog.setContent(updatedBlog.getContent());
            }
            if (updatedBlog.getCategory() != null) {
                existingBlog.setCategory(updatedBlog.getCategory());
            }
            if (updatedBlog.getImage() != null) {
                existingBlog.setImage(updatedBlog.getImage());
            }

            return blogRepository.save(existingBlog);
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }



    @Override
    public List<Blog> getBlogsByCategory(String category) {
        try {
            CategoryEnum categoryEnum = CategoryEnum.valueOf(category.toUpperCase()); // Convert input to enum
            return blogRepository.findByCategory(categoryEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category, e);
        }
    }

}
