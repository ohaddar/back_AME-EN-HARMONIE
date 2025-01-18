package com.doranco.project.controllers;

import com.doranco.project.entities.Blog;
import com.doranco.project.services.BlogService;
import com.doranco.project.utils.FileUpload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Blogs")
public class BlogController {
    @Autowired

      BlogService blogService;
@Autowired
 FileUpload fileUpload;

    @PostMapping("/save")
    public ResponseEntity<Blog> saveBlogs(@RequestParam("image") MultipartFile file, @RequestParam("blog") String blogJson, Authentication authentication) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Blog blog = objectMapper.readValue(blogJson, Blog.class);

            byte[] imageData = fileUpload.uploadFile(file);
            blog.setImage(imageData);
            Blog savedBlog = blogService.saveBlog(blog);
            return ResponseEntity.ok(savedBlog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id, Authentication authentication) {
        Optional<Blog> blogsById = blogService.getBlogById(id);
        if(blogsById.isPresent()) {
            Blog blog = blogsById.get();
            blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
        }

        return blogsById.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/blogs")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Blog>> getAllBlogs( Authentication authentication) {

        List<Blog> blogs = blogService.getBlog();
        for (Blog blog : blogs) {
            blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
        }

        return ResponseEntity.ok(blogs);

    }
    @GetMapping("/public")
    public ResponseEntity<List<Blog>> getPublicBlogs() {
        List<Blog> blogs = blogService.getBlog();

        // Take only the first two blogs
        List<Blog> limitedBlogs = blogs.stream()
                .limit(2)
                .collect(Collectors.toList());

        for (Blog blog : limitedBlogs) {
            blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
        }

        return ResponseEntity.ok(limitedBlogs);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Blog> blog = blogService.getBlogById(id);

        if (blog.isPresent() && blog.get().getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image.jpg\"")
                    .body(blog.get().getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBlogById(@PathVariable Long id) {
        try {
            blogService.deleteBlogById(id);
            return ResponseEntity.noContent().build(); // 204 No Content on success
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBlog(@PathVariable Long id,
                                        @RequestParam(value = "image", required = false) MultipartFile file,
                                        @RequestParam("blog") String blogJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Blog updatedBlog = objectMapper.readValue(blogJson, Blog.class);

            // Handle file upload if a new image is provided
            if (file != null && !file.isEmpty()) {
                if (!isValidImage(file)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid image file");
                }
                byte[] imageData = fileUpload.uploadFile(file); // Implement proper error handling here
                updatedBlog.setImage(imageData);
            }

            // Update blog in database
            Blog savedBlog = blogService.updateBlogById(id, updatedBlog);
            return ResponseEntity.ok(savedBlog);
        } catch (RuntimeException e) {
            // Log the error and return a meaningful message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Blog not found with ID: " + id);
        } catch (IOException e) {
            // Log the error and provide feedback
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid blog JSON format");
        } catch (Exception e) {
            // Log and return a generic error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // Helper method to validate image file (optional but recommended)
    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBlogsByCategory(@PathVariable String category, Authentication authentication) {
        try {
            List<Blog> blogs = blogService.getBlogsByCategory(category);
            for (Blog blog : blogs) {
                blog.setImageUrl("http://localhost:8080/Blogs/image/" + blog.getId());
            }
            return ResponseEntity.ok(blogs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
