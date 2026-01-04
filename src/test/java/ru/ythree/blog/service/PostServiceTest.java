package ru.ythree.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.ythree.blog.model.Page;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.repository.PostRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PostService.class)
class PostServiceTest {

    @MockitoBean
    private PostRepository repository;

    @Autowired
    private PostService service;

    @BeforeEach
    void setUp() {
        clearInvocations(repository);
    }

    @Test
    void findPage() {
        Post post1 = new Post(11L, "Название поста 1", "Текст поста в формате Markdown...", new String[]{"tag_1", "tag_2"}, 5, 2);
        Post post2 = new Post(12L, "Название поста 2", "Текст поста в формате Markdown...", new String[]{}, 1, 1);
        Post post3 = new Post(13L, "Название 3", "Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq", new String[]{"tag_3"}, 0, 0);

        List<Post> posts = Arrays.asList(post1, post2, post3);

        when(repository.count()).thenReturn(posts.size());

        String search = "";
        int pageNumber = 1;
        int pageSize = 5;

        when(repository.findAll(any(), any(), any())).thenReturn(posts);

        Page page = service.findPage(search, pageNumber, pageSize);
        assertNotNull(page);
        assertEquals(3, page.getPosts().size());
        assertEquals(1, page.getLastPage());
        assertFalse(page.isHasPrev());
        assertFalse(page.isHasNext());

        verify(repository).count();
        verify(repository).findAll(any(), any(), any());
    }

    @Test
    void find() {
        long postId = 11L;

        when(repository.find(postId))
                .thenReturn(new Post(postId, "title", "text", new String[]{}, 1, 2));

        Post post = service.find(postId);
        assertNotNull(post);
        assertEquals(postId, post.getId());
        assertEquals("title", post.getTitle());
        assertEquals("text", post.getText());
        assertEquals(0, post.getTags().length);
        assertEquals(1, post.getLikesCount());
        assertEquals(2, post.getCommentsCount());

        verify(repository).find(postId);
    }

    @Test
    void save() {
        long postId = 11L;

        Post post = new Post(null, "title", "text", new String[]{}, 1, 2);
        doAnswer(in -> {
            Post postInSave = in.getArgument(0);
            postInSave.setId(11L);
            return postInSave;
        }).when(repository).save(post);


        Post savedPost = service.save(post);
        assertEquals(postId, savedPost.getId());

        verify(repository).save(post);
    }

    @Test
    void update() {
        long postId = 11L;

        Post post = new Post(postId, "new title", "new text", new String[]{}, 1, 2);
        doNothing().when(repository).update(postId, post);


        Post updatedPost = service.update(postId, post);
        assertEquals(postId, updatedPost.getId());
        assertEquals("new title", updatedPost.getTitle());

        verify(repository).update(postId, post);
    }

    @Test
    void deleteById() {
        long postId = 11L;

        doNothing().when(repository).deleteById(postId);

        service.deleteById(postId);

        verify(repository).deleteById(postId);
    }

    @Test
    void updateLikes() {
        long postId = 11L;

        doNothing().when(repository).updateLikes(postId);
        when(repository.find(postId)).thenReturn(new Post(postId, "title", "text", new String[]{}, 3, 2));

        long likes = service.updateLikes(postId);
        assertEquals(3, likes);

        verify(repository).updateLikes(postId);
        verify(repository).find(postId);
    }
}