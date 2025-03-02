package com.doranco.project.servicesImp;

import com.doranco.project.dto.BlogDTO;
import com.doranco.project.entities.Blog;
import com.doranco.project.enums.CategoryEnum;
import com.doranco.project.repositories.IBlogRepository;
import com.doranco.project.utils.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class BlogServiceImpTest {

    @Mock
    private IBlogRepository blogRepository;

    @Mock
    private FileUpload fileUpload;

    @InjectMocks
    private BlogServiceImp blogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testSaveBlog() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("Test content");

        when(blogRepository.save(any(Blog.class))).thenReturn(blog);
        when(fileUpload.uploadFile(any(MultipartFile.class))).thenReturn(new byte[0]);

        BlogDTO savedBlog = blogService.saveBlog("{\"title\": \"Test Blog\", \"content\": \"Test content\"}", null);

        assert savedBlog.getTitle().equals("Test Blog");
        assert savedBlog.getContent().equals("Test content");
    }

    @Test
    void testGetBlogById() {
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("Test content");
        blog.setId(1L);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        Optional<BlogDTO> result = blogService.getBlogById(1L);

        assert result.isPresent();
        assert result.get().getTitle().equals("Test Blog");
    }

    @Test
    void testGetBlogsByCategory() {
        Blog blog = new Blog();
        blog.setCategory(CategoryEnum.DEPRESSION);

        when(blogRepository.findByCategory(CategoryEnum.DEPRESSION)).thenReturn(List.of(blog));

        var blogs = blogService.getBlogsByCategory("DEPRESSION");

        assert blogs.size() == 1;
        assert blogs.getFirst().getCategory() == CategoryEnum.DEPRESSION;
    }
}
