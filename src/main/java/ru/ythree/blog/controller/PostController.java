package ru.ythree.blog.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ythree.blog.model.Comment;
import ru.ythree.blog.model.Page;
import ru.ythree.blog.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @GetMapping
    public Page getPage(@RequestParam(name = "search") String search,
                        @RequestParam(name = "pageNumber") String pageNumber,
                        @RequestParam(name = "pageSize") String pageSize) {

        //search — это строка из поля поиска, pageNumber — номер текущей страницы, pageSize — число постов на странице

        Post post1 = new Post(1L, "Название поста 1", "Текст поста в формате Markdown...", Arrays.asList("tag_1", "tag_2"), 5, 1);
        Post post2 = new Post(2L, "Название поста 2", "Текст поста в формате Markdown...", new ArrayList<>(), 1, 5);

        List<Post> posts = Arrays.asList(post1, post2);

        return new Page(posts, true, false, 3);
    }

    @GetMapping("/{id}")
    public List<Post> getPost(@PathVariable(name = "id") Long id) {
        Post post = new Post(1L, "Название поста 1", "Текст поста в формате Markdown...", Arrays.asList("tag_1", "tag_2"), 5, 1);

        return Collections.singletonList(post);
    }

    @PostMapping
    public Post savePost(@RequestBody Post post) {
        post.setId(3L);
        return post;
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable(name = "id") Long id, @RequestBody Post post) {
        return post;
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable(name = "id") Long id) {

    }

    @PostMapping("/{id}/likes")
    public long addLike(@PathVariable(name = "id") Long id) {
        //todo Бэкенд должен добавить +1 к числу лайков поста и вернуть обновлённое число лайков поста (число в теле ответа)
        return 666;
    }

    @PutMapping("/{id}/image")
    public void updateImage() {
        //todo параметры запроса
    }

    @GetMapping("/{id}/image")
    public void getImage() {
        //todo параметры запроса и ответ
        //Бэкенд должен вернуть массив байт картинки поста в теле ответа.
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@PathVariable(name = "id") Long id) {
        return Arrays.asList(new Comment(1L, "Комментарий к посту 1", 1),
                new Comment(2L, "Ещё один комментарий к посту 1", 1));
    }

    @GetMapping("/{id}/comments/{commentId}")
    public Comment getComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId) {
        return new Comment(1L, "Комментарий к посту 1", 1);
    }

    @PostMapping("/{id}/comments")
    public Comment saveComment(@PathVariable(name = "id") Long id, @RequestBody Comment comment) {
        comment.setId(666L);
        return comment;
    }

    @PutMapping("/{id}/comments/{commentId}")
    public Comment updateComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId,
                                 @RequestBody Comment comment) {
        return comment;
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId) {
    }
}
