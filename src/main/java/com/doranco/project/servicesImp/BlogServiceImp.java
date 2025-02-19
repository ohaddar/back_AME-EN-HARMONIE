package com.doranco.project.servicesImp;

import com.doranco.project.entities.Blog;
import com.doranco.project.enums.CategoryEnum;
import com.doranco.project.repositories.IBlogRepository;
import com.doranco.project.services.BlogService;
import com.doranco.project.utils.FileUpload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service

public class BlogServiceImp implements BlogService {

    @Autowired
    IBlogRepository blogRepository;

    @Autowired
    private FileUpload fileUpload;

    @Override
    public Blog saveBlog(String blogJson, MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Blog blog = objectMapper.readValue(blogJson, Blog.class);

            if (file != null && !file.isEmpty()) {
                byte[] imageData = fileUpload.uploadFile(file);
                blog.setImage(imageData);
            }

            blog.setCreationDate(new Date());
            return blogRepository.save(blog);
        } catch (Exception e) {
            throw new RuntimeException("Error saving blog", e);
        }

    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        Optional<Blog> blogById = blogRepository.findById(id);
        if(blogById.isPresent()) {
            Blog blog = blogById.get();
            blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
        }
        return blogById;

    }

    @Override
    public List<Blog> getAllBlogs() {
         List<Blog> allBlogs = blogRepository.findAll();
        for (Blog blog : allBlogs) {
            blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
        }
        return allBlogs;

    }

    @Override
    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }



    @Override
    public Blog updateBlogById(Long id, String blogJson, MultipartFile file) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog existingBlog = optionalBlog.get();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Blog updatedBlog = objectMapper.readValue(blogJson, Blog.class);

                if (updatedBlog.getTitle() != null) existingBlog.setTitle(updatedBlog.getTitle());
                if (updatedBlog.getContent() != null) existingBlog.setContent(updatedBlog.getContent());
                if (updatedBlog.getCategory() != null) existingBlog.setCategory(updatedBlog.getCategory());

                if (file != null && !file.isEmpty()) {
                    byte[] imageData = fileUpload.uploadFile(file);
                    existingBlog.setImage(imageData);
                }

                return blogRepository.save(existingBlog);
            } catch (Exception e) {
                throw new RuntimeException("Error updating blog", e);
            }
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }


    @Override
    public List<Blog> getBlogsByCategory(String category) {
        try {
            CategoryEnum categoryEnum = CategoryEnum.valueOf(category.toUpperCase());
              List<Blog> blogs =  blogRepository.findByCategory(categoryEnum);
            for (Blog blog : blogs) {
                blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
            }
            return blogs;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category, e);
        }
    }


}
