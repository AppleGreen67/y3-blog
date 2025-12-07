package ru.ythree.blog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.ythree.blog.config.DataSourceConfiguration;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.model.SearchFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class,
        JdbcNativePostRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("delete from posts");
        jdbcTemplate.execute("delete from tags");
        jdbcTemplate.execute("delete from comments");

        jdbcTemplate.execute("""
                    insert into posts(id, title, text, likesCount)
                    values (11, 'Название поста 1', 'Текст поста в формате Markdown', 5)
                """);
        jdbcTemplate.execute("""
                    insert into posts(id, title, text, likesCount)
                    values (12, 'Название поста 2', 'Текст поста в формате Markdown', 1)
                """);
        jdbcTemplate.execute("""
                    insert into posts(id, title, text, likesCount)
                    values (13, 'Название 3', 'Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq', 0)
                """);

        jdbcTemplate.execute("insert into tags(tag, post_id) values ('tag_1', 11)");
        jdbcTemplate.execute("insert into tags(tag, post_id) values ('tag_2', 11)");

        jdbcTemplate.execute("insert into tags(tag, post_id) values ('tag_3', 13)");

        jdbcTemplate.execute("insert into comments(id, text, post_id) values (11, 'Комментарий 1 к посту 1', 11)");
        jdbcTemplate.execute("insert into comments(id, text, post_id) values (12, 'Комментарий 2 к посту 1', 11)");

        jdbcTemplate.execute("insert into comments (id, text, post_id) values (13, 'Комментарий 1 к посту 2', 12)");
    }

    @Test
    void count() {
        assertEquals(3, postRepository.count());
    }

    @Test
    void findAll() {
        List<Post> posts = postRepository.findAll(new SearchFilter(), 0, 5);
        assertEquals(3, posts.size());
    }

    @Test
    void findAll_firstPage() {
        List<Post> posts = postRepository.findAll(new SearchFilter(), 0, 1);
        assertEquals(1, posts.size());
        assertEquals(11, posts.getFirst().getId());
    }

    @Test
    void findAll_secondPage() {
        List<Post> posts = postRepository.findAll(new SearchFilter(), 1, 1);
        assertEquals(1, posts.size());
        assertEquals(12, posts.getFirst().getId());
    }

    @Test
    void findAll_thirdPage() {
        List<Post> posts = postRepository.findAll(new SearchFilter(), 2, 1);
        assertEquals(1, posts.size());
        assertEquals(13, posts.getFirst().getId());
    }

    @Test
    void findAll_searchStr() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setSearchStr("поста");

        List<Post> posts = postRepository.findAll(searchFilter, 0, 5);
        assertEquals(2, posts.size());
        assertEquals(11, posts.get(0).getId());
        assertEquals(12, posts.get(1).getId());
    }

    @Test
    void findAll_searchTag() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setTags(Collections.singletonList("tag_3"));

        List<Post> posts = postRepository.findAll(searchFilter, 0, 5);
        assertEquals(1, posts.size());
        assertEquals(13, posts.getFirst().getId());
    }

    @Test
    void findAll_searchStrTag() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setSearchStr("поста");
        searchFilter.setTags(Collections.singletonList("tag_1"));

        List<Post> posts = postRepository.findAll(searchFilter, 0, 5);
        assertEquals(1, posts.size());
        assertEquals(11, posts.getFirst().getId());
    }

    @Test
    void findAll_empty() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setSearchStr("поста");
        searchFilter.setTags(Collections.singletonList("tag_3"));

        List<Post> posts = postRepository.findAll(searchFilter, 0, 5);
        assertTrue(posts.isEmpty());
    }

    @Test
    void find() {
        Post post = postRepository.find(11L);
        assertNotNull(post);
        assertEquals(11, post.getId());
        assertEquals("Название поста 1", post.getTitle());
        assertEquals("Текст поста в формате Markdown", post.getText());
        assertEquals(2, post.getTags().length);
        assertEquals("tag_1", post.getTags()[0]);
        assertEquals("tag_2", post.getTags()[1]);
        assertEquals(5, post.getLikesCount());
        assertEquals(2, post.getCommentsCount());
    }

    @Test
    void save() {
        assertEquals(3, postRepository.findAll(new SearchFilter(), 0, 5).size());

        Post newPost = new Post(null, "new post title", "new post text", new String[]{}, 0, 0);
        postRepository.save(newPost);

        assertNotNull(newPost.getId());
        assertEquals(4, postRepository.findAll(new SearchFilter(), 0, 5).size());
    }

    @Test
    void update() {
        Post post = postRepository.find(11L);
        assertNotNull(post);
        assertEquals(11, post.getId());
        assertEquals("Название поста 1", post.getTitle());
        assertEquals("Текст поста в формате Markdown", post.getText());
        assertEquals(2, post.getTags().length);
        assertEquals("tag_1", post.getTags()[0]);
        assertEquals("tag_2", post.getTags()[1]);
        assertEquals(5, post.getLikesCount());
        assertEquals(2, post.getCommentsCount());

        Post updatedPost = new Post(11L, "Новое название поста 1", "Новый текст", new String[]{"tag45"}, 7, 8);
        postRepository.update(11L, updatedPost);

        post = postRepository.find(11L);
        assertNotNull(post);
        assertEquals(11, post.getId());
        assertEquals("Новое название поста 1", post.getTitle());
        assertEquals("Новый текст", post.getText());
        assertEquals(1, post.getTags().length);
        assertEquals("tag45", post.getTags()[0]);
        assertEquals(5, post.getLikesCount());
        assertEquals(2, post.getCommentsCount());
    }

    @Test
    void deleteById() {
        assertEquals(3, postRepository.findAll(new SearchFilter(), 0, 5).size());

        postRepository.deleteById(12L);

        assertEquals(2, postRepository.findAll(new SearchFilter(), 0, 5).size());
    }

    @Test
    void updateLikes() {
        Post post = postRepository.find(11L);
        assertEquals(11, post.getId());
        assertEquals(5, post.getLikesCount());

        postRepository.updateLikes(11L);

        post = postRepository.find(11L);
        assertEquals(11, post.getId());
        assertEquals(6, post.getLikesCount());
    }
}