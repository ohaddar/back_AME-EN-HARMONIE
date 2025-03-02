package com.doranco.project.servicesImp;

import com.doranco.project.dto.BlogDTO;
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
import java.util.stream.Collectors;

@Service

public class BlogServiceImp implements BlogService {

    @Autowired
    IBlogRepository blogRepository;

    @Autowired
    private FileUpload fileUpload;

    @Override
    public BlogDTO saveBlog(String blogJson, MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Blog blog = objectMapper.readValue(blogJson, Blog.class);

            if (file != null && !file.isEmpty()) {
                byte[] imageData = fileUpload.uploadFile(file);
                blog.setImage(imageData);
            }

            blog.setCreationDate(new Date());
            Blog savedBlog = blogRepository.save(blog);
            return new BlogDTO(savedBlog);
        } catch (Exception e) {
            throw new RuntimeException("Error saving blog", e);
        }

    }

    @Override
    public Optional<BlogDTO> getBlogById(Long id) {
        Optional<Blog> blogById = blogRepository.findById(id);

       return Optional.of(new BlogDTO(blogById.get()));
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
         List<Blog> allBlogs = blogRepository.findAll();
        return allBlogs.stream()
                .map(BlogDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public BlogDTO updateBlogById(Long id, String blogJson, MultipartFile file) {
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

                Blog blog = blogRepository.save(existingBlog);
                return  new BlogDTO(blog);
            } catch (Exception e) {
                throw new RuntimeException("Error updating blog", e);
            }
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }

    @Override
    public List<BlogDTO> getBlogsByCategory(String category) {
        try {
            CategoryEnum categoryEnum = CategoryEnum.valueOf(category.toUpperCase());
              List<Blog> blogs =  blogRepository.findByCategory(categoryEnum);

            return blogs.stream()
                    .map(BlogDTO::new)
                    .collect(Collectors.toList());        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category, e);
        }
    }
}
