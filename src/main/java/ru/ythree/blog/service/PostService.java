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
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Page findPage(String search, String pageNumber, String pageSize) {
        /**
         * Строка поиска разбивается на слова по пробелам.
         * Пустые слова удаляются из поиска.
         * Слова, начинающиеся с #, считаются тегами, и посты фильтруются по ним по «И».
         * Слова, не начинающиеся с #, склеиваются вместе через пробел и считаются подстрокой поиска по названию.
         * Фильтрация постов по подстроке и тегам происходит по «И».
         * Если число постов на странице превышает выбранное значение (селект в правом верхнем углу), то становятся доступными кнопки внизу страницы перехода
         *
         *
         * текст поста (в формате Markdown, если больше 128 символов, то обрезается до 128 символов и добавляется «…»)
         * */

        List<Post> posts = repository.findAll();
        return new Page(posts, true, false, 3);
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
