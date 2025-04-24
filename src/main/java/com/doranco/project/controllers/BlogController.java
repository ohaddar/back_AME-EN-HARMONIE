package com.doranco.project.controllers;

import com.doranco.project.dto.BlogDTO;
import com.doranco.project.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Blogs")
public class BlogController {

    @Autowired
    BlogService blogService;


    @PostMapping("/save")
    public ResponseEntity<BlogDTO> saveBlog(@RequestParam("image") MultipartFile file, @RequestParam("blog") String blogJson, Authentication authentication) {
        try {
            BlogDTO savedBlog = blogService.saveBlog(blogJson, file);
            return ResponseEntity.ok(savedBlog);
           } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable String id, Authentication authentication) {
        Optional<BlogDTO> blogsById = blogService.getBlogById(id);
        return blogsById.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/blogs")
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {

        List<BlogDTO> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogById(@PathVariable String id) {
        try {
            blogService.deleteBlogById(id);
            return ResponseEntity.noContent().build(); // 204 No Content on success
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable String id,
                                        @RequestParam(value = "image", required = false) MultipartFile file,
                                        @RequestParam("blog") String blogJson) {

        try {
            BlogDTO updatedBlog = blogService.updateBlogById(id, blogJson, file);
            return ResponseEntity.ok(updatedBlog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }



    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBlogsByCategory(@PathVariable String category, Authentication authentication) {

            List<BlogDTO> blogs = blogService.getBlogsByCategory(category);
            return ResponseEntity.ok(blogs);
    }

}
