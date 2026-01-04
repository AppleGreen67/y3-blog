package ru.ythree.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ythree.blog.model.Comment;
import ru.ythree.blog.model.Page;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.service.CommentService;
import ru.ythree.blog.service.FilesService;
import ru.ythree.blog.service.PostService;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private FilesService filesService;

    private final Post post1 = new Post(11L, "Название поста 1", "Текст поста в формате Markdown...", new String[]{"tag_1", "tag_2"}, 5, 2);
    private final Post post2 = new Post(12L, "Название поста 2", "Текст поста в формате Markdown...", new String[]{}, 1, 1);
    private final Post post3 = new Post(13L, "Название 3", "Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq", new String[]{"tag_3"}, 0, 0);

    private final Comment comment1 = new Comment(11L, "Комментарий 1 к посту 1", 11L);
    private final Comment comment2 = new Comment(12L, "Комментарий 2 к посту 1", 11L);
    private final Comment comment3 = new Comment(13L, "Комментарий 1 к посту 2", 12L);

    @BeforeEach
    void beforeTest() {
        clearInvocations(postService);
        clearInvocations(commentService);
        clearInvocations(filesService);
    }

    @Test
    void getPost_handleException() throws Exception {
        long postId = 11L;
        when(postService.find(postId)).thenThrow(new RuntimeException("Some exception in post service"));

        mockMvc.perform(get("/api/posts/{postId}", postId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getPage_noSearch_onePage() throws Exception {
        when(postService.findPage("", 1, 5))
                .thenReturn(new Page(Arrays.asList(post1, post2, post3), false, false, 1));

        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(3)))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(1));

        verify(postService).findPage("", 1, 5);
    }

    @Test
    void getPage_noSearch_firstPage_size1() throws Exception {
        when(postService.findPage("", 1, 1))
                .thenReturn(new Page(Collections.singletonList(post1), false, true, 3));

        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(1)))
                .andExpect(jsonPath("$.posts[0].id").value(11))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.lastPage").value(3));

        verify(postService).findPage("", 1, 1);
    }

    @Test
    void getPage_noSearch_firstPage_size2() throws Exception {
        when(postService.findPage("", 2, 1))
                .thenReturn(new Page(Collections.singletonList(post2), true, true, 3));

        mockMvc.perform(get("/api/posts?search=&pageNumber=2&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(1)))
                .andExpect(jsonPath("$.posts[0].id").value(12))
                .andExpect(jsonPath("$.hasPrev").value(true))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.lastPage").value(3));

        verify(postService).findPage("", 2, 1);
    }

    @Test
    void getPage_noSearch_firstPage_size3() throws Exception {
        when(postService.findPage("", 3, 1))
                .thenReturn(new Page(Collections.singletonList(post3), true, false, 3));

        mockMvc.perform(get("/api/posts?search=&pageNumber=3&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(1)))
                .andExpect(jsonPath("$.posts[0].id").value(13))
                .andExpect(jsonPath("$.hasPrev").value(true))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(3));

        verify(postService).findPage("", 3, 1);
    }

    @Test
    void getPage_searchStr() throws Exception {
        when(postService.findPage("Название поста", 1, 5))
                .thenReturn(new Page(Arrays.asList(post1, post2), false, false, 1));

        mockMvc.perform(get("/api/posts?search=Название поста&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(2)))
                .andExpect(jsonPath("$.posts[0].id").value(11))
                .andExpect(jsonPath("$.posts[1].id").value(12))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(1));

        verify(postService).findPage("Название поста", 1, 5);
    }

    @Test
    void getPage_searchTag() throws Exception {
        when(postService.findPage("#tag_3", 1, 5))
                .thenReturn(new Page(Arrays.asList(post3), false, false, 1));

        mockMvc.perform(get("/api/posts?search={search}&pageNumber=1&pageSize=5", "#tag_3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts", hasSize(1)))
                .andExpect(jsonPath("$.posts[0].id").value(13))
                .andExpect(jsonPath("$.posts[0].title").value("Название 3"))
                .andExpect(jsonPath("$.posts[0].text").value("Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq"))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(1));

        verify(postService).findPage("#tag_3", 1, 5);
    }

    @Test
    void getPost() throws Exception {
        long postId = 11L;
        when(postService.find(postId)).thenReturn(post1);

        mockMvc.perform(get("/api/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Название поста 1"))
                .andExpect(jsonPath("$.text").value("Текст поста в формате Markdown..."))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.likesCount").value(5))
                .andExpect(jsonPath("$.commentsCount").value(2));

        verify(postService).find(postId);
    }

    @Test
    void getPost_longText() throws Exception {
        long postId = 13L;
        when(postService.find(postId)).thenReturn(post3);

        mockMvc.perform(get("/api/posts/13"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Название 3"))
                .andExpect(jsonPath("$.text").value("Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq"));

        verify(postService).find(postId);
    }

    @Test
    void savePost() throws Exception {
        String json = """
                  {"title":"post title","text":"post text","tags":["tag_1"]}
                """;

        Post post4 = new Post(1L, "post title", "post text", new String[]{"tag_1"}, 0, 0);
        when(postService.save(any(Post.class))).thenReturn(post4);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title").value("post title"));

        verify(postService).save(any(Post.class));
    }

    @Test
    void updatePost() throws Exception {
        String json = """
                  {"id":11,"title":"new post title","text":"new post text","tags":["tag_1", "tag_2"]}
                """;

        long postId = 11L;
        when(postService.update(eq(postId), any(Post.class)))
                .thenReturn(new Post(11L, "new post title", "new post text", new String[]{"tag_1", "tag_2"}, 5, 2));

        mockMvc.perform(put("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.title").value("new post title"));

        verify(postService).update(eq(postId), any(Post.class));
    }

    @Test
    void deletePost() throws Exception {
        long postId = 13L;
        doNothing().when(postService).deleteById(postId);

        mockMvc.perform(delete("/api/posts/{postId}", postId))
                .andExpect(status().isOk());

        verify(postService).deleteById(postId);
    }

    @Test
    void updateLikes() throws Exception {
        long postId = 11L;
        when(postService.updateLikes(postId)).thenReturn(1L);

        mockMvc.perform(post("/api/posts/{postId}/likes", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));

        verify(postService).updateLikes(postId);
    }

    @Test
    void updateAndGetImage() throws Exception {
        long postId = 11L;
        byte[] pngStub = new byte[]{(byte) 137, 80, 78, 71};
        MockMultipartFile file = new MockMultipartFile("image", "post.png", "image/png", pngStub);

        doNothing().when(filesService).upload(file, postId);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/{postId}/image", postId).file(file))
                .andExpect(status().isOk());

        verify(filesService).upload(file, postId);

    }

    @Test
    void getComments() throws Exception {
        long postId = 11L;

        when(commentService.findAll(postId)).thenReturn(Arrays.asList(comment1, comment2));

        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(11))
                .andExpect(jsonPath("$[0].text").value("Комментарий 1 к посту 1"))
                .andExpect(jsonPath("$[0].postId").value(11))
                .andExpect(jsonPath("$[1].id").value(12))
                .andExpect(jsonPath("$[1].text").value("Комментарий 2 к посту 1"))
                .andExpect(jsonPath("$[1].postId").value(11));

        verify(commentService).findAll(postId);
    }

    @Test
    void getComment() throws Exception {
        long postId = 11L;
        long commentId = 11L;

        when(commentService.find(postId, commentId)).thenReturn(comment1);

        mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.text").value("Комментарий 1 к посту 1"))
                .andExpect(jsonPath("$.postId").value(11));

        verify(commentService).find(postId, commentId);
    }

    @Test
    void saveComment() throws Exception {
        long postId = 11L;

        String json = """
                  {"text":"comment text","postId":11}
                """;

        Comment comment4 = new Comment(14L, "comment text", 11L);
        when(commentService.save(eq(postId), any(Comment.class))).thenReturn(comment4);

        mockMvc.perform(post("/api/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text").value("comment text"))
                .andExpect(jsonPath("$.postId").value(11));

        verify(commentService).save(eq(postId), any(Comment.class));
    }

    @Test
    void updateComment() throws Exception {
        long postId = 11L;
        long commentId = 11L;
        String json = """
                  {"id":11,"text":"new comment text","postId":11}
                """;

        when(commentService.update(eq(postId), eq(commentId), any(Comment.class)))
                .thenReturn(new Comment(11L, "new comment text", 11L));

        mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.text").value("new comment text"));

        verify(commentService).update(eq(postId), eq(commentId), any(Comment.class));
    }

    @Test
    void deleteComment() throws Exception {
        long postId = 11L;
        long commentId = 11L;

        doNothing().when(commentService).delete(postId, commentId);

        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk());

        verify(commentService).delete(postId, commentId);
    }
}