package ru.ythree.blog.repository;

import org.springframework.web.multipart.MultipartFile;
import ru.ythree.blog.model.Image;

import java.util.Optional;

public interface ImageRepository {
    Optional<Image> findByPostId(Long postId);

    void save(String imageName, MultipartFile file, Long postId);

    void update(String imageName, MultipartFile file, Long postId);
}
