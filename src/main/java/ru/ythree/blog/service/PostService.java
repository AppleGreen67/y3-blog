package ru.ythree.blog.service;

import org.springframework.stereotype.Service;
import ru.ythree.blog.model.Page;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.repository.PostRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PostService {
    private static final int max_post_len = 128;

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Page findPage(String search, Integer pageNumber, Integer pageSize) {
        /**
         * Строка поиска разбивается на слова по пробелам.
         * Пустые слова удаляются из поиска.
         * Слова, начинающиеся с #, считаются тегами, и посты фильтруются по ним по «И».
         * Слова, не начинающиеся с #, склеиваются вместе через пробел и считаются подстрокой поиска по названию.
         * Фильтрация постов по подстроке и тегам происходит по «И».
         * Если число постов на странице превышает выбранное значение (селект в правом верхнем углу), то становятся доступными кнопки внизу страницы перехода
         *
         * */

        Integer size = repository.count();

        int offset = (pageNumber - 1) * pageSize;
        List<Post> posts = repository.findAll(offset, pageSize);
        posts.stream()
                .filter(post -> post.getText() != null && post.getText().length() > max_post_len)
                .forEach(post -> post.setText(post.getText().substring(0, max_post_len) + "..."));

        int lastPage = (size / pageSize) + (size % pageSize == 0 ? 0 : 1);
        return new Page(posts, pageNumber > 1, pageNumber < lastPage, lastPage);
    }

    public Post find(Long id) {
        return repository.find(id);
    }

    public Post save(Post post) {
        repository.save(post);
        return post;
    }

    public Post update(Long id, Post post) {
        repository.update(id, post);
        return post;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long updateLikes(Long id) {
        repository.updateLikes(id);
        return repository.find(id).getLikesCount();
    }
}
