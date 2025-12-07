package ru.ythree.blog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ythree.blog.model.Post;
import ru.ythree.blog.model.SearchFilter;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Integer count() {
        return jdbcTemplate.queryForObject("select count(*) from posts p", Integer.class);
    }

    @Override
    public List<Post> findAll(SearchFilter searchFilter, int offset, int size) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", size);
        params.addValue("offset", offset);

        StringBuilder sql = new StringBuilder("""
                select p.*, listagg(t.tag) within group (order by t.tag) as tags_list, count(distinct c.id) as commentsCount
                from posts p
                left join tags t ON p.id=t.post_id
                left join comments c on p.id=c.post_id
                """);
        if (searchFilter.getSearchStr() != null || searchFilter.getTags() != null) {
            sql.append(" where ");
        }
        if (searchFilter.getSearchStr() != null) {
            sql.append(" p.title like :titleSearch ");
            params.addValue("titleSearch", "%" + searchFilter.getSearchStr() + "%");
        }

        if (searchFilter.getTags() != null) {
            if (params.hasValue("titleSearch"))
                sql.append(" and ");

            String tagsFilter = """
                    select pp.id
                    from posts pp
                    join tags tt on pp.id = tt.post_id
                    where tt.tag in (:required_tags)
                    group by pp.id
                    having count(distinct tt.tag)=:required_tags_count
                    """;
            sql.append("p.id in (")
                    .append(tagsFilter)
                    .append(")");

            params.addValue("required_tags", searchFilter.getTags());
            params.addValue("required_tags_count", searchFilter.getTags().size());
        }

        sql.append("""
                 group by p.id
                order by p.id
                limit :limit
                offset :offset
                """);

        Map<Long, Post> posts = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql.toString(), params, postRowHandler(posts));
        return new ArrayList<>(posts.values());
    }

    @Override
    public Post find(Long id) {
        String sql = """
                select p.*, listagg(t.tag) within group (order by t.tag) as tags_list, count(distinct c.id) as commentsCount
                from posts p
                left join tags t ON p.id=t.post_id
                left join comments c on p.id=c.post_id
                where p.id=?
                group by p.id
                order by p.id
                """;

        Map<Long, Post> posts = new LinkedHashMap<>();
        jdbcTemplate.query(sql, new Object[]{id}, postRowHandler(posts));

        return posts.get(id);
    }

    @Override
    @Transactional
    public void save(Post post) {
        String sql = "insert into posts(title, text, likesCount) values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setLong(3, post.getLikesCount());
            return ps;
        }, keyHolder);

        post.setId((Long) keyHolder.getKeys().get("id"));

        saveTags(post);
    }

    @Override
    @Transactional
    public void update(Long id, Post post) {
        jdbcTemplate.update("update posts set title=?, text=?, likesCount=? where id=?",
                post.getTitle(), post.getText(), post.getLikesCount(), id);

        jdbcTemplate.update("delete from tags where post_id=?", id);

        saveTags(post);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from posts where id=?", id);
    }

    @Override
    public void updateLikes(Long id) {
        jdbcTemplate.update("update posts set likesCount=likesCount+1 where id=?", id);
    }

    private void saveTags(Post post) {
        String sql_tag = "insert into tags(tag, post_id) values(?, ?)";
        String[] tags = post.getTags();
        for (String tag : tags) {
            jdbcTemplate.update(sql_tag, tag, post.getId());
        }
    }

    private RowCallbackHandler postRowHandler(Map<Long, Post> posts) {
        return (rs) -> {
            Long postId = rs.getLong("id");

            Post post = posts.get(postId);
            if (post == null) {
                String tags = rs.getString("tags_list");
                post = new Post(postId,
                        rs.getString("title"),
                        rs.getString("text"),
                        tags == null ? new String[]{} : tags.split(","),
                        rs.getInt("likesCount"),
                        rs.getInt("commentsCount"));
                posts.put(postId, post);
            }
        };
    }
}
