package ru.ythree.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.ythree.blog.model.Image;
import ru.ythree.blog.repository.ImageRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = FilesService.class)
class FilesServiceTest {

    @MockitoBean
    private ImageRepository repository;

    @Autowired
    private FilesService service;

    @BeforeEach
    void setUp() {
        clearInvocations(repository);
    }

    @Test
    void upload_save() throws IOException {
        long postId = 11L;

        MockMultipartFile multipartFile = new MockMultipartFile("name", "post.png", "image/png", new byte[]{(byte) 137, 80, 78, 71});

        when(repository.findByPostId(postId)).thenReturn(Optional.empty());

        service.upload(multipartFile, postId);

        verify(repository).save(multipartFile.getOriginalFilename(), multipartFile.getBytes(), postId);
    }

    @Test
    void upload_update() throws IOException {
        long postId = 11L;

        MockMultipartFile multipartFile = new MockMultipartFile("new name", "post.png", "image/png", new byte[]{(byte) 137, 80, 78, 71});

        Image image = new Image("name", new byte[]{(byte) 137, 80, 78, 71}, postId);
        when(repository.findByPostId(postId)).thenReturn(Optional.of(image));

        service.upload(multipartFile, postId);

        verify(repository).update(multipartFile.getOriginalFilename(), multipartFile.getBytes(), postId);
    }

    @Test
    void download() throws IOException {
        long postId = 11L;

        Image image = new Image("name", new byte[]{(byte) 137, 80, 78, 71}, postId);
        when(repository.findByPostId(postId)).thenReturn(Optional.of(image));

        Resource download = service.download(postId);
        assertNotNull(download);
        assertArrayEquals(image.getData(), download.getContentAsByteArray());

        verify(repository).findByPostId(postId);
    }
}