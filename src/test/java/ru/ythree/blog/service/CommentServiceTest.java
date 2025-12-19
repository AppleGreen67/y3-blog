package ru.ythree.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.ythree.blog.model.Comment;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.repository.CommentRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CommentService.class)
class CommentServiceTest {

    @MockitoBean
    private CommentRepository repository;

    @Autowired
    private CommentService service;

    private final Comment comment1 = new Comment(11L, "Комментарий 1 к посту 1", 11L);
    private final Comment comment2 = new Comment(12L, "Комментарий 2 к посту 1", 11L);
    private final Comment comment3 = new Comment(13L, "Комментарий 1 к посту 2", 12L);

    @BeforeEach
    void setUp() {
        clearInvocations(repository);
    }

    @Test
    void findAll() {
        long postId = 11L;

        when(repository.findAll(postId)).thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> comments = service.findAll(postId);
        assertEquals(2, comments.size());

        verify(repository).findAll(postId);
    }

    @Test
    void find() {
        long postId = 11L;
        long commentId = 12L;

        when(repository.find(postId, commentId)).thenReturn(comment2);

        Comment comment = repository.find(postId, commentId);
        assertEquals(12L, comment.getId());

        verify(repository).find(postId, commentId);
    }

    @Test
    void save() {
        long postId = 12L;
        long commentId = 14L;

        Comment comment4 = new Comment(null, "Комментарий 2 к посту 2", postId);
        doAnswer(in -> {
            Comment commentInSave = in.getArgument(0);
            commentInSave.setId(commentId);
            return commentInSave;
        }).when(repository).save(comment4);


        Comment savedComment = service.save(postId, comment4);
        assertEquals(commentId, savedComment.getId());

        verify(repository).save(comment4);
    }

    @Test
    void update() {
        long postId = 11L;
        long commentId = 12L;

        Comment comment4 = new Comment(commentId, "Новый комментарий", postId);
        doNothing().when(repository).update(postId, commentId, comment4);


        Comment updateComment = service.update(postId, commentId, comment4);
        assertEquals(commentId, updateComment.getId());
        assertEquals("Новый комментарий", updateComment.getText());

        verify(repository).update(postId, commentId, comment4);
    }

    @Test
    void delete() {
        long postId = 11L;
        long commentId = 12L;

        doNothing().when(repository).deleteById(postId, commentId);

        service.delete(postId, commentId);

        verify(repository).deleteById(postId, commentId);
    }
}