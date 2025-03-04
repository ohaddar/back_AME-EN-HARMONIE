package com.doranco.project.servicesImp;

import com.doranco.project.dto.BlogDTO;
import com.doranco.project.enums.CategoryEnum;
import com.doranco.project.repositories.IBlogRepository;
import com.doranco.project.services.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BlogServiceIntegrationTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private IBlogRepository blogRepository;

    @BeforeEach
    public void setup() {
        blogRepository.deleteAll();
    }

    @Test
    public void testSaveBlogWithFile() {
        String blogJson = "{\"title\":\"Test Blog\",\"content\":\"This is a test blog\",\"category\":\"DEPRESSION\"}";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes(StandardCharsets.UTF_8)
        );

        BlogDTO savedBlog = blogService.saveBlog(blogJson, file);

        assertNotNull(savedBlog);
        assertNotNull(savedBlog.getId());
        assertEquals("Test Blog", savedBlog.getTitle());
        assertEquals("This is a test blog", savedBlog.getContent());
        assertEquals(CategoryEnum.DEPRESSION, savedBlog.getCategory());
        assertNotNull(savedBlog.getImageBlob());
        assertNotNull(savedBlog.getCreationDate());
    }

    @Test
    public void testSaveBlogWithoutFile() {
        String blogJson = "{\"title\":\"Blog Without File\",\"content\":\"No file provided\",\"category\":\"ADDICTION\"}";
        BlogDTO savedBlog = blogService.saveBlog(blogJson, null);

        assertNotNull(savedBlog);
        assertNotNull(savedBlog.getId());
        assertEquals("Blog Without File", savedBlog.getTitle());
        assertEquals("No file provided", savedBlog.getContent());
        assertEquals(CategoryEnum.ADDICTION, savedBlog.getCategory());
        assertNull(savedBlog.getImageBlob());
        assertNotNull(savedBlog.getCreationDate());
    }

    @Test
    public void testGetBlogById() {
        String blogJson = "{\"title\":\"Blog To Retrieve\",\"content\":\"Content for retrieval\",\"category\":\"DEPRESSION\"}";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "retrieve.jpg",
                "image/jpeg",
                "image content".getBytes(StandardCharsets.UTF_8)
        );
        BlogDTO savedBlog = blogService.saveBlog(blogJson, file);
        Long id = savedBlog.getId();

        Optional<BlogDTO> retrievedOpt = blogService.getBlogById(id);
        assertTrue(retrievedOpt.isPresent());
        BlogDTO retrievedBlog = retrievedOpt.get();
        assertEquals("Blog To Retrieve" + id, retrievedBlog.getTitle());
    }

    @Test
    public void testGetAllBlogs() {
        String blogJson1 = "{\"title\":\"Blog 1\",\"content\":\"Content 1\",\"category\":\"DEPRESSION\"}";
        String blogJson2 = "{\"title\":\"Blog 2\",\"content\":\"Content 2\",\"category\":\"PHOBIE_SPECIFIQUE\"}";
        blogService.saveBlog(blogJson1, null);
        blogService.saveBlog(blogJson2, null);

        List<BlogDTO> allBlogs = blogService.getAllBlogs();
        assertNotNull(allBlogs);
        assertEquals(2, allBlogs.size());
        allBlogs.forEach(blog ->
                assertEquals("Blog " + blog.getId(), blog.getTitle())
        );
    }

    @Test
    public void testUpdateBlogByIdWithFile() {
        String blogJson = "{\"title\":\"Original Title\",\"content\":\"Original content\",\"category\":\"DEPRESSION\"}";
        BlogDTO savedBlog = blogService.saveBlog(blogJson, null);
        Long id = savedBlog.getId();

        String updateJson = "{\"title\":\"Updated Title\",\"content\":\"Updated content\",\"category\":\"TOC\"}";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "update.jpg",
                "image/jpeg",
                "updated image content".getBytes(StandardCharsets.UTF_8)
        );

        BlogDTO updatedBlog = blogService.updateBlogById(id, updateJson, file);
        assertNotNull(updatedBlog);
        assertEquals("Updated Title", updatedBlog.getTitle());
        assertEquals("Updated content", updatedBlog.getContent());
        assertEquals(CategoryEnum.TOC, updatedBlog.getCategory());
        assertNotNull(updatedBlog.getImageBlob());
    }

    @Test
    public void testUpdateBlogByIdWithoutFile() {
        String blogJson = "{\"title\":\"Original Title\",\"content\":\"Original content\",\"category\":\"PHOBIE_SPECIFIQUE\"}";
        BlogDTO savedBlog = blogService.saveBlog(blogJson, null);
        Long id = savedBlog.getId();

        String updateJson = "{\"title\":\"Updated Title\",\"content\":\"Updated content\",\"category\":\"PHOBIE\"}";
        BlogDTO updatedBlog = blogService.updateBlogById(id, updateJson, null);
        assertNotNull(updatedBlog);
        assertEquals("Updated Title", updatedBlog.getTitle());
        assertEquals("Updated content", updatedBlog.getContent());
        assertEquals(CategoryEnum.PHOBIE, updatedBlog.getCategory());
        assertNull(updatedBlog.getImageBlob());
    }

    @Test
    public void testUpdateBlogById_NotFound() {
        String updateJson = "{\"title\":\"Updated Title\",\"content\":\"Updated content\",\"category\":\"DEPRESSION\"}";
        Long nonExistentId = 999L;
        Exception exception = assertThrows(RuntimeException.class, () ->
                blogService.updateBlogById(nonExistentId, updateJson, null)
        );
        assertTrue(exception.getMessage().contains("Blog not found with id"));
    }

    @Test
    public void testDeleteBlogById() {
        String blogJson = "{\"title\":\"Blog To Delete\",\"content\":\"Delete this blog\",\"category\":\"DEPRESSION\"}";
        BlogDTO savedBlog = blogService.saveBlog(blogJson, null);
        Long id = savedBlog.getId();

        blogService.deleteBlogById(id);
        Optional<BlogDTO> retrievedOpt = blogService.getBlogById(id);
        assertFalse(retrievedOpt.isPresent());
    }

    @Test
    public void testGetBlogsByCategory() {
        String blogJson1 = "{\"title\":\"DEPRESSION Blog\",\"content\":\"DEPRESSION content\",\"category\":\"DEPRESSION\"}";
        String blogJson2 = "{\"title\":\"PHOBIE_SPECIFIQUE Blog\",\"content\":\"PHOBIE_SPECIFIQUE content\",\"category\":\"TROUBLE_PANIQUE\"}";
        blogService.saveBlog(blogJson1, null);
        blogService.saveBlog(blogJson2, null);

        List<BlogDTO> blogsByCategory = blogService.getBlogsByCategory("TROUBLE_PANIQUE");
        assertNotNull(blogsByCategory);
        assertTrue(blogsByCategory.size() >= 1);
        blogsByCategory.forEach(blog -> {
            assertEquals(CategoryEnum.TROUBLE_PANIQUE, blog.getCategory());
        });

        Exception exception = assertThrows(RuntimeException.class, () ->
                blogService.getBlogsByCategory("INVALID_CATEGORY")
        );
        assertTrue(exception.getMessage().contains("Invalid category"));
    }
}
