package com.doranco.project.controllers;

import com.doranco.project.dto.BlogDTO;
import com.doranco.project.entities.Blog;
import com.doranco.project.enums.CategoryEnum;
import com.doranco.project.services.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }

    @Test
    void testSaveBlog() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("Test content");
        blog.setId("1");

        BlogDTO blogDTO= new BlogDTO(blog);

        when(blogService.saveBlog(any(String.class), any())).thenReturn(blogDTO);

        mockMvc.perform(multipart("/Blogs/save")
                        .file("image", "imageData".getBytes())
                        .param("blog", "{\"title\": \"Test Blog\", \"content\": \"Test content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Blog"))
                .andExpect(jsonPath("$.content").value("Test content"));

        verify(blogService, times(1)).saveBlog(any(String.class), any());
    }

    @Test
    void testGetBlogById() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("Test content");
        blog.setId("1");

        BlogDTO blogDTO= new BlogDTO(blog);

        when(blogService.getBlogById("1")).thenReturn(Optional.of(blogDTO));

        mockMvc.perform(get("/Blogs/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Blog"))
                .andExpect(jsonPath("$.content").value("Test content"));

        verify(blogService, times(1)).getBlogById("1");
    }

    @Test
    void testGetBlogById_NotFound() throws Exception {
        when(blogService.getBlogById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/Blogs/{id}", "1"))
                .andExpect(status().isNotFound());

        verify(blogService, times(1)).getBlogById("1");
    }

    @Test
    void testGetAllBlogs() throws Exception {
        Blog blog1 = new Blog();
        blog1.setTitle("Blog 1");
        Blog blog2 = new Blog();
        blog2.setTitle("Blog 2");

        BlogDTO blogDTO1= new BlogDTO(blog1);
        BlogDTO blogDTO2= new BlogDTO(blog2);

        when(blogService.getAllBlogs()).thenReturn(Arrays.asList(blogDTO1, blogDTO2));

        mockMvc.perform(get("/Blogs/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Blog 1"))
                .andExpect(jsonPath("$[1].title").value("Blog 2"));

        verify(blogService, times(1)).getAllBlogs();
    }

    @Test
    void testDeleteBlogById() throws Exception {
        doNothing().when(blogService).deleteBlogById("1");

        mockMvc.perform(delete("/Blogs/{id}", "1"))
                .andExpect(status().isNoContent());

        verify(blogService, times(1)).deleteBlogById("1");
    }

    @Test
    void testUpdateBlog() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("Updated Blog");

        BlogDTO blogDTO= new BlogDTO(blog);

        when(blogService.updateBlogById(any(String.class), any(String.class), any())).thenReturn(blogDTO);

        mockMvc.perform(put("/Blogs/update/{id}", "1")
                        .param("blog", "{\"title\": \"Updated Blog\", \"content\": \"Updated content\"}")
                        .param("image", "imageData"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Blog"));

        verify(blogService, times(1)).updateBlogById(any(String.class), any(String.class), any());
    }

    @Test
    void testGetBlogsByCategory() throws Exception {
        Blog blog = new Blog();
        blog.setCategory(CategoryEnum.DEPRESSION);

        BlogDTO blogDTO= new BlogDTO(blog);

        when(blogService.getBlogsByCategory("DEPRESSION")).thenReturn(List.of(blogDTO));

        mockMvc.perform(get("/Blogs/category/{category}", "DEPRESSION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("DEPRESSION"));

        verify(blogService, times(1)).getBlogsByCategory("DEPRESSION");
    }
}
