package com.doranco.project.controllers;

import com.doranco.project.entities.Blog;
import com.doranco.project.services.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Blogs")
public class BlogController {

    public final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/save")
    public ResponseEntity<Blog> saveBlogs(@RequestBody Blog blog) {
        Blog savedBlogs = blogService.saveBlog(blog);
        return ResponseEntity.ok(savedBlogs);

    }
    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        Optional<Blog> blogsById = blogService.getBlogById(id);
        return blogsById.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getBlog();

        return ResponseEntity.ok(blogs);

    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Blog>> getBlogsByTopic(@PathVariable String title) {
        List<Blog> blogs = blogService.getBlogsByTopic(title);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = blogService.getBlogsByCategory(category);
        return ResponseEntity.ok(blogs);
    }

}
