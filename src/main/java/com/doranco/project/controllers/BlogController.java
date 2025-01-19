package com.doranco.project.controllers;

import com.doranco.project.entities.Blog;
import com.doranco.project.services.BlogService;
import com.doranco.project.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Blog> saveBlogs(@RequestParam("image") MultipartFile file, @RequestParam("blog") String blogJson, Authentication authentication) {
        try {
            Blog savedBlog = blogService.saveBlog(blogJson, file);
            return ResponseEntity.ok(savedBlog);
           } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
    }

    @GetMapping("/{id}")

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id, Authentication authentication) {
        Optional<Blog> blogsById = blogService.getBlogById(id);
        return blogsById.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/blogs")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Blog>> getAllBlogs( Authentication authentication) {

        List<Blog> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);

    }
    @GetMapping("/public")
    public ResponseEntity<List<Blog>> getPublicBlogs() {
        List<Blog> limitedBlogs = blogService.getPublicBlogs();
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
            Blog updatedBlog = blogService.updateBlogById(id, blogJson, file);
            return ResponseEntity.ok(updatedBlog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }



    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBlogsByCategory(@PathVariable String category, Authentication authentication) {

            List<Blog> blogs = blogService.getBlogsByCategory(category);
            return ResponseEntity.ok(blogs);
    }

}
