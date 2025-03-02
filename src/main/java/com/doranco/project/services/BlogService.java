package com.doranco.project.services;

import com.doranco.project.dto.BlogDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Service
public interface BlogService {
    BlogDTO saveBlog(String blogJson, MultipartFile file);
    Optional<BlogDTO> getBlogById(Long id);
    List<BlogDTO> getAllBlogs() ;
    void deleteBlogById(Long id);
    BlogDTO updateBlogById(Long id, String blogJson, MultipartFile file);
    List<BlogDTO> getBlogsByCategory(String category);
}
