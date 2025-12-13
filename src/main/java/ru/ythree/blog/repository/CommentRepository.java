package ru.ythree.blog.repository;

import ru.ythree.blog.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAll(Long id);

    Comment find(Long id, Long commentId);

    void save(Comment comment);

    void update(Long id, Long commentId, Comment comment);

    void deleteById(Long id, Long commentId);
}
