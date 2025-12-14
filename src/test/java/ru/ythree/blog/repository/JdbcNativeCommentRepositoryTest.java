//package ru.ythree.blog.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import ru.ythree.blog.config.DataSourceConfiguration;
//import ru.ythree.blog.model.Comment;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringJUnitConfig(classes = {DataSourceConfiguration.class,
//        JdbcNativeCommentRepository.class})
//@TestPropertySource(locations = "classpath:test-application.properties")
//class JdbcNativeCommentRepositoryTest {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @BeforeEach
//    void setUp() {
//        jdbcTemplate.execute("delete from posts");
//        jdbcTemplate.execute("delete from comments");
//
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (11, 'Название поста 1', 'Текст поста в формате Markdown', 5)
//                """);
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (12, 'Название поста 2', 'Текст поста в формате Markdown', 1)
//                """);
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (13, 'Название 3', 'Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq', 0)
//                """);
//
//        jdbcTemplate.execute("insert into comments(id, text, post_id) values (11, 'Комментарий 1 к посту 1', 11)");
//        jdbcTemplate.execute("insert into comments(id, text, post_id) values (12, 'Комментарий 2 к посту 1', 11)");
//
//        jdbcTemplate.execute("insert into comments (id, text, post_id) values (13, 'Комментарий 1 к посту 2', 12)");
//    }
//
//
//    @Test
//    void findAll() {
//        List<Comment> comments = commentRepository.findAll(11L);
//        assertEquals(2, comments.size());
//
//        assertEquals(11, comments.get(0).getId());
//        assertEquals("Комментарий 1 к посту 1", comments.get(0).getText());
//        assertEquals(11, comments.get(0).getPostId());
//
//
//        assertEquals(12, comments.get(1).getId());
//        assertEquals("Комментарий 2 к посту 1", comments.get(1).getText());
//        assertEquals(11, comments.get(1).getPostId());
//    }
//
//    @Test
//    void find() {
//        Comment comment = commentRepository.find(11L, 12L);
//        assertNotNull(comment);
//        assertEquals(12, comment.getId());
//        assertEquals("Комментарий 2 к посту 1", comment.getText());
//        assertEquals(11, comment.getPostId());
//    }
//
//    @Test
//    void save() {
//        assertEquals(2, commentRepository.findAll(11L).size());
//
//        Comment comment = new Comment(null, "Комметарий для поста 11", 11L);
//        commentRepository.save(comment);
//
//        assertNotNull(comment.getId());
//        assertEquals(3, commentRepository.findAll(11L).size());
//    }
//
//    @Test
//    void update() {
//        List<Comment> comments = commentRepository.findAll(11L);
//        assertEquals(2, comments.size());
//
//        assertEquals(11, comments.getFirst().getId());
//        assertEquals("Комментарий 1 к посту 1", comments.getFirst().getText());
//        assertEquals(11, comments.getFirst().getPostId());
//
//
//        Comment updatedComment = new Comment(11L, "Новый комментарий", 11L);
//        commentRepository.update(11L, 11L, updatedComment);
//
//
//        comments = commentRepository.findAll(11L);
//        assertEquals(2, comments.size());
//
//        assertEquals(11, comments.getFirst().getId());
//        assertEquals("Новый комментарий", comments.getFirst().getText());
//        assertEquals(11, comments.getFirst().getPostId());
//    }
//
//    @Test
//    void deleteById() {
//        List<Comment> comments = commentRepository.findAll(11L);
//        assertEquals(2, comments.size());
//        assertEquals(11, comments.get(0).getId());
//        assertEquals(12, comments.get(1).getId());
//
//        commentRepository.deleteById(11L, 11L);
//
//        comments = commentRepository.findAll(11L);
//        assertEquals(1, comments.size());
//        assertEquals(12, comments.get(0).getId());
//
//    }
//}