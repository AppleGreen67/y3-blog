package ru.ythree.blog.repository;

import ru.ythree.blog.model.Post;

public interface PostRepository {
    Post find(Long id);

    void save(Post post);

    void update(Long id, Post post);

    void deleteById(Long id);
}
