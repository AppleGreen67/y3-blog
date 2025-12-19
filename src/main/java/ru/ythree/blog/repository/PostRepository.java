package ru.ythree.blog.repository;

import ru.ythree.blog.model.Post;
import ru.ythree.blog.model.SearchFilter;

import java.util.List;

public interface PostRepository {
    Integer count();

    List<Post> findAll(SearchFilter searchFilter, Integer offset, Integer size);

    Post find(Long id);

    void save(Post post);

    void update(Long id, Post post);

    void deleteById(Long id);

    void updateLikes(Long id);
}
