package ru.ythree.blog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import ru.ythree.blog.model.Post;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        String sql = """
                select p.*, t.id as tag_id, t.tag, count(distinct c.id) as commentsCount
                from posts p
                left join tags t ON p.id=t.post_id
                left join comments c on p.id=c.post_id
                group by p.id, t.id
                """;

        Map<Long, Post> posts = new LinkedHashMap<>();
        jdbcTemplate.query(sql, postRowHandler(posts));

        return new ArrayList<>(posts.values());
    }

    @Override
    public Post find(Long id) {
        String sql = """
                select p.*, t.id as tag_id, t.tag, count(distinct c.id) as commentsCount
                from posts p
                left join tags t ON p.id=t.post_id
                left join comments c on p.id=c.post_id
                where p.id=?
                group by p.id, t.id
                """;

        Map<Long, Post> posts = new LinkedHashMap<>();
        jdbcTemplate.query(sql, new Object[]{id}, postRowHandler(posts));

        return posts.get(id);
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update("insert into posts(title, text, likesCount) values(?, ?, ?)",
                post.getTitle(), post.getText(), post.getLikesCount());
    }

    @Override
    public void update(Long id, Post post) {
        jdbcTemplate.update("update posts set title=?, text=?, likesCount=? where id=?",
                post.getTitle(), post.getText(), post.getLikesCount(), id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from posts where id=?", id);
    }

    @Override
    public void updateLikes(Long id) {
        jdbcTemplate.update("update posts set likesCount=likesCount+1 where id=?", id);
    }

    private RowCallbackHandler postRowHandler(Map<Long, Post> posts) {
        return (rs) -> {
            Long postId = rs.getLong("id");

            Post post = posts.get(postId);
            if (post == null) {
                post = new Post(postId,
                        rs.getString("title"),
                        rs.getString("text"),
                        new ArrayList<>(),
                        rs.getInt("likesCount"),
                        rs.getInt("commentsCount"));
                posts.put(postId, post);
            }

            if (rs.getInt("tag_id") != 0)
                post.getTags().add(rs.getString("tag"));
        };
    }
}
