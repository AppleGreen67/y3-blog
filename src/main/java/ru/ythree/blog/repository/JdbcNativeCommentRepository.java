package ru.ythree.blog.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ythree.blog.model.Comment;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        String sql = "insert into comments(text, post_id) values(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            return ps;
        }, keyHolder);

        comment.setId((Long) keyHolder.getKeys().get("id"));
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
