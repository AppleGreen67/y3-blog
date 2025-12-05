package ru.ythree.blog.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ythree.blog.model.Comment;

import java.util.List;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAll(Long id) {
        String sql = """
                select c.* from comments c
                where c.post_id=?
                """;
        return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Comment.class));
    }

    @Override
    public Comment find(Long id, Long commentId) {
        String sql = """
                select c.* from comments c
                where c.id=? and c.post_id=?
                """;
        return jdbcTemplate.queryForObject(sql, new Object[]{commentId, id}, new BeanPropertyRowMapper<>(Comment.class));
    }

    @Override
    public void save(Comment comment) {
        jdbcTemplate.update("insert into comments(text, post_id) values(?, ?)",
                comment.getText(), comment.getPostId());
    }

    @Override
    public void update(Long id, Long commentId, Comment comment) {
        jdbcTemplate.update("update comments set text=? where id=?", comment.getText(), commentId);
    }

    @Override
    public void deleteById(Long id, Long commentId) {
        jdbcTemplate.update("delete from comments where id=?", commentId);
    }
}
