package ru.ythree.blog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.ythree.blog.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbsNativeImageRepository implements ImageRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbsNativeImageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Image> findByPostId(Long postId) {
        String sql = """
                select i.*
                from images i
                where post_id=?
                """;

        List<Image> images = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[]{postId}, (rs) -> {
            Image image = new Image();
            image.setName(rs.getString("name"));
            image.setData(rs.getBytes("data"));
            image.setPostId(rs.getLong("post_id"));

            images.add(image);
        });
        return Optional.ofNullable(images.isEmpty() ? null : images.getFirst());
    }

    @Override
    public void save(String imageName, MultipartFile file, Long postId) {
        try {
            jdbcTemplate.update("insert into images(name, data, post_id) values(?, ?, ?)",
                    imageName, file.getBytes(), postId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String imageName, MultipartFile file, Long postId) {
        try {
            jdbcTemplate.update("update images set name=?, data=? where post_id=?",
                    imageName, file.getBytes(), postId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
