//package ru.ythree.blog.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import ru.ythree.blog.config.DataSourceConfiguration;
//import ru.ythree.blog.model.Image;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringJUnitConfig(classes = {DataSourceConfiguration.class,
//        JdbsNativeImageRepository.class})
//@TestPropertySource(locations = "classpath:test-application.properties")
//class JdbsNativeImageRepositoryTest {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private ImageRepository imageRepository;
//
//    private byte[] imageBytes = {1, 2, 3, 4};
//
//    @BeforeEach
//    void setUp() {
//        jdbcTemplate.execute("delete from posts");
//        jdbcTemplate.execute("delete from images");
//
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (11, 'Название поста 1', 'Текст поста в формате Markdown', 5)
//                """);
//
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (12, 'Название поста 2', 'Текст поста в формате Markdown', 1)
//                """);
//
//        jdbcTemplate.update("insert into images(name, data, post_id) values(?, ?, ?)",
//                "imageName.png", imageBytes, 11L);
//    }
//
//    @Test
//    void findByPostId() {
//        Optional<Image> image = imageRepository.findByPostId(11L);
//
//        assertTrue(image.isPresent());
//        assertEquals("imageName.png", image.get().getName());
//        assertEquals(11, image.get().getPostId());
//        assertArrayEquals(imageBytes, image.get().getData());
//    }
//
//    @Test
//    void save() {
//        Optional<Image> image = imageRepository.findByPostId(12L);
//
//        assertFalse(image.isPresent());
//
//        byte[] newImage = new byte[]{1, 2, 3, 4};
//
//        imageRepository.save("i.png", newImage, 12L);
//
//        image = imageRepository.findByPostId(12L);
//        assertTrue(image.isPresent());
//        assertEquals("i.png", image.get().getName());
//        assertEquals(12, image.get().getPostId());
//        assertArrayEquals(newImage, image.get().getData());
//    }
//
//    @Test
//    void update() {
//        Optional<Image> image = imageRepository.findByPostId(11L);
//
//        assertTrue(image.isPresent());
//        assertEquals("imageName.png", image.get().getName());
//        assertEquals(11, image.get().getPostId());
//        assertArrayEquals(imageBytes, image.get().getData());
//
//
//        byte[] newImage = new byte[]{1, 2, 3, 4};
//        imageRepository.update("ii.png", newImage, 11L);
//
//        image = imageRepository.findByPostId(11L);
//        assertTrue(image.isPresent());
//        assertEquals("ii.png", image.get().getName());
//        assertEquals(11, image.get().getPostId());
//        assertArrayEquals(newImage, image.get().getData());
//    }
//}