package ru.ythree.blog.controller;

import org.springframework.web.bind.annotation.*;
import ru.ythree.blog.model.Comment;
import ru.ythree.blog.model.Page;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.service.PostService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public Page getPage(@RequestParam(name = "search") String search,
                        @RequestParam(name = "pageNumber") String pageNumber,
                        @RequestParam(name = "pageSize") String pageSize) {
        //search — это строка из поля поиска, pageNumber — номер текущей страницы, pageSize — число постов на странице
        return service.findPage(search, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable(name = "id") Long id) {
        return service.find(id);
    }

    @PostMapping
    public Post savePost(@RequestBody Post post) {
        post.setId(3L);
        return service.save(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable(name = "id") Long id, @RequestBody Post post) {
        return service.update(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable(name = "id") Long id) {
        service.deleteById(id);
    }

    @PostMapping("/{id}/likes")
    public long updateLikes(@PathVariable(name = "id") Long id) {
        return service.updateLikes(id);
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
