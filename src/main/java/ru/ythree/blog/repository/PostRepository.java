package ru.ythree.blog.repository;

import ru.ythree.blog.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();

    Post find(Long id);

    void save(Post post);

    void update(Long id, Post post);

    void deleteById(Long id);
}
