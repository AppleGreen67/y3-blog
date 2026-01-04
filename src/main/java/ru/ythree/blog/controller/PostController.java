package ru.ythree.blog.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ythree.blog.model.Comment;
import ru.ythree.blog.model.Page;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.service.CommentService;
import ru.ythree.blog.service.FilesService;
import ru.ythree.blog.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final FilesService filesService;

    public PostController(PostService postService, CommentService commentService, FilesService filesService) {
        this.postService = postService;
        this.commentService = commentService;
        this.filesService = filesService;
    }

    @GetMapping
    public Page getPage(@RequestParam(name = "search") String search,
                        @RequestParam(name = "pageNumber") String pageNumber,
                        @RequestParam(name = "pageSize") String pageSize) {
        return postService.findPage(search, Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable(name = "id") Long id) {
        return postService.find(id);
    }

    @PostMapping
    public Post savePost(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable(name = "id") Long id, @RequestBody Post post) {
        return postService.update(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable(name = "id") Long id) {
        postService.deleteById(id);
    }

    @PostMapping("/{id}/likes")
    public long updateLikes(@PathVariable(name = "id") Long id) {
        return postService.updateLikes(id);
    }

    @PutMapping("/{id}/image")
    public void updateImage(@PathVariable(name = "id") Long id, @RequestParam("image") MultipartFile file) {
        filesService.upload(file, id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable(name = "id") Long id) {
        Resource file = filesService.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@PathVariable(name = "id") Long id) {
        return commentService.findAll(id);
    }

    @GetMapping("/{id}/comments/{commentId}")
    public Comment getComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId) {
        return commentService.find(id, commentId);
    }

    @PostMapping("/{id}/comments")
    public Comment saveComment(@PathVariable(name = "id") Long id, @RequestBody Comment comment) {
        return commentService.save(id, comment);
    }

    @PutMapping("/{id}/comments/{commentId}")
    public Comment updateComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId,
                                 @RequestBody Comment comment) {
        return commentService.update(id, commentId, comment);
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId) {
        commentService.delete(id, commentId);
    }
}
