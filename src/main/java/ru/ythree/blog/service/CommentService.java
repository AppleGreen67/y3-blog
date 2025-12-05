package ru.ythree.blog.service;

import org.springframework.stereotype.Service;
import ru.ythree.blog.model.Comment;
import ru.ythree.blog.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public List<Comment> findAll(Long id) {
        return repository.findAll(id);
    }

    public Comment find(Long id, Long commentId) {
        return repository.find(id, commentId);
    }

    public Comment save(Long id, Comment comment) {
        repository.save(comment);
        return comment;
    }

    public Comment update(Long id, Long commentId, Comment comment) {
        repository.update(id, commentId, comment);
        return comment;
    }

    public void delete(Long id, Long commentId) {
        repository.deleteById(id, commentId);
    }
}