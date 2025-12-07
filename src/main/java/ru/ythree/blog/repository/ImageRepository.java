package ru.ythree.blog.repository;

import ru.ythree.blog.model.Image;

import java.util.Optional;

public interface ImageRepository {
    Optional<Image> findByPostId(Long postId);

    void save(String imageName, byte[] file, Long postId);

    void update(String imageName, byte[] file, Long postId);
}
