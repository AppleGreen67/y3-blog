//package ru.ythree.blog.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import ru.ythree.blog.config.ApplicationConfig;
//import ru.ythree.blog.config.DataSourceConfiguration;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringJUnitConfig(classes = {
//        DataSourceConfiguration.class,
//        ApplicationConfig.class,
//})
//@WebAppConfiguration
//@TestPropertySource(locations = "classpath:test-application.properties")
//class PostControllerTest {
//
//
//    @Autowired
//    private WebApplicationContext wac;
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//
//        jdbcTemplate.execute("delete from posts");
//        jdbcTemplate.execute("delete from tags");
//        jdbcTemplate.execute("delete from comments");
//
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (11, 'Название поста 1', 'Текст поста в формате Markdown...', 5)
//                """);
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (12, 'Название поста 2', 'Текст поста в формате Markdown...', 1)
//                """);
//        jdbcTemplate.execute("""
//                    insert into posts(id, title, text, likesCount)
//                    values (13, 'Название 3', 'Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq', 0)
//                """);
//
//        jdbcTemplate.execute("insert into tags(tag, post_id) values ('tag_1', 11)");
//        jdbcTemplate.execute("insert into tags(tag, post_id) values ('tag_2', 11)");
//
//        jdbcTemplate.execute("insert into tags(tag, post_id) values ('tag_3', 13)");
//
//        jdbcTemplate.execute("insert into comments(id, text, post_id) values (11, 'Комментарий 1 к посту 1', 11)");
//        jdbcTemplate.execute("insert into comments(id, text, post_id) values (12, 'Комментарий 2 к посту 1', 11)");
//
//        jdbcTemplate.execute("insert into comments (id, text, post_id) values (13, 'Комментарий 1 к посту 2', 12)");
//    }
//
//    @Test
//    void getPage_noSearch_onePage() throws Exception {
//        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(3)))
//                .andExpect(jsonPath("$.hasPrev").value(false))
//                .andExpect(jsonPath("$.hasNext").value(false))
//                .andExpect(jsonPath("$.lastPage").value(1));
//    }
//
//    @Test
//    void getPage_noSearch_firstPage_size1() throws Exception {
//        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(1)))
//                .andExpect(jsonPath("$.posts[0].id").value(11))
//                .andExpect(jsonPath("$.hasPrev").value(false))
//                .andExpect(jsonPath("$.hasNext").value(true))
//                .andExpect(jsonPath("$.lastPage").value(3));
//    }
//
//    @Test
//    void getPage_noSearch_firstPage_size2() throws Exception {
//        mockMvc.perform(get("/api/posts?search=&pageNumber=2&pageSize=1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(1)))
//                .andExpect(jsonPath("$.posts[0].id").value(12))
//                .andExpect(jsonPath("$.hasPrev").value(true))
//                .andExpect(jsonPath("$.hasNext").value(true))
//                .andExpect(jsonPath("$.lastPage").value(3));
//    }
//
//    @Test
//    void getPage_noSearch_firstPage_size3() throws Exception {
//        mockMvc.perform(get("/api/posts?search=&pageNumber=3&pageSize=1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(1)))
//                .andExpect(jsonPath("$.posts[0].id").value(13))
//                .andExpect(jsonPath("$.hasPrev").value(true))
//                .andExpect(jsonPath("$.hasNext").value(false))
//                .andExpect(jsonPath("$.lastPage").value(3));
//    }
//
//    @Test
//    void getPage_searchStr() throws Exception {
//        mockMvc.perform(get("/api/posts?search=Название поста&pageNumber=1&pageSize=5"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(2)))
//                .andExpect(jsonPath("$.posts[0].id").value(11))
//                .andExpect(jsonPath("$.posts[1].id").value(12))
//                .andExpect(jsonPath("$.hasPrev").value(false))
//                .andExpect(jsonPath("$.hasNext").value(false))
//                .andExpect(jsonPath("$.lastPage").value(1));
//    }
//
//    @Test
//    void getPage_searchTag() throws Exception {
//        mockMvc.perform(get("/api/posts?search={search}&pageNumber=1&pageSize=5", "#tag_3"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(1)))
//                .andExpect(jsonPath("$.posts[0].id").value(13))
//                .andExpect(jsonPath("$.posts[0].title").value("Название 3"))
//                .andExpect(jsonPath("$.posts[0].text").value("Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffff..."))
//                .andExpect(jsonPath("$.hasPrev").value(false))
//                .andExpect(jsonPath("$.hasNext").value(false))
//                .andExpect(jsonPath("$.lastPage").value(1));
//    }
//
//    @Test
//    void getPost() throws Exception {
//        mockMvc.perform(get("/api/posts/11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.title").value("Название поста 1"))
//                .andExpect(jsonPath("$.text").value("Текст поста в формате Markdown..."))
//                .andExpect(jsonPath("$.tags", hasSize(2)))
//                .andExpect(jsonPath("$.likesCount").value(5))
//                .andExpect(jsonPath("$.commentsCount").value(2));
//    }
//
//    @Test
//    void getPost_longText() throws Exception {
//        mockMvc.perform(get("/api/posts/13"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(13))
//                .andExpect(jsonPath("$.title").value("Название 3"))
//                .andExpect(jsonPath("$.text").value("Текст поста в формате Markdowntttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttfffffffffffffftqqq"));
//    }
//
//    @Test
//    void savePost() throws Exception {
//        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(3)));
//
//        String json = """
//                  {"title":"post title","text":"post text","tags":["tag_1"]}
//                """;
//
//        mockMvc.perform(post("/api/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.title").value("post title"));
//
//        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(4)));
//    }
//
//    @Test
//    void updatePost() throws Exception {
//        mockMvc.perform(get("/api/posts/11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.title").value("Название поста 1"));
//
//        String json = """
//                  {"id":11,"title":"new post title","text":"new post text","tags":["tag_1", "tag_2"]}
//                """;
//
//        mockMvc.perform(put("/api/posts/11")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.title").value("new post title"));
//    }
//
//    @Test
//    void deletePost() throws Exception {
//        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(3)));
//
//
//        mockMvc.perform(delete("/api/posts/13"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.posts", hasSize(2)));
//    }
//
//    @Test
//    void updateLikes() throws Exception {
//        mockMvc.perform(get("/api/posts/11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.likesCount").value(5));
//
//        mockMvc.perform(post("/api/posts/11/likes"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        mockMvc.perform(get("/api/posts/11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.likesCount").value(6));
//    }
//
//    @Test
//    void updateAndGetImage() throws Exception {
//        byte[] pngStub = new byte[]{(byte) 137, 80, 78, 71};
//        MockMultipartFile file = new MockMultipartFile("image", "post.png", "image/png", pngStub);
//
//        mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/11/image").file(file))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/posts/11/image", 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
//                .andExpect(content().bytes(pngStub));
//    }
//
//    @Test
//    void getComments() throws Exception {
//        mockMvc.perform(get("/api/posts/11/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id").value(11))
//                .andExpect(jsonPath("$[0].text").value("Комментарий 1 к посту 1"))
//                .andExpect(jsonPath("$[0].postId").value(11))
//                .andExpect(jsonPath("$[1].id").value(12))
//                .andExpect(jsonPath("$[1].text").value("Комментарий 2 к посту 1"))
//                .andExpect(jsonPath("$[1].postId").value(11));
//    }
//
//    @Test
//    void getComment() throws Exception {
//        mockMvc.perform(get("/api/posts/11/comments/11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.text").value("Комментарий 1 к посту 1"))
//                .andExpect(jsonPath("$.postId").value(11));
//    }
//
//    @Test
//    void saveComment() throws Exception {
//        mockMvc.perform(get("/api/posts/11/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)));
//
//        String json = """
//                  {"text":"comment text","postId":11}
//                """;
//
//        mockMvc.perform(post("/api/posts/11/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.text").value("comment text"))
//                .andExpect(jsonPath("$.postId").value(11));
//
//        mockMvc.perform(get("/api/posts/11/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(3)));
//    }
//
//    @Test
//    void updateComment() throws Exception {
//        mockMvc.perform(get("/api/posts/11/comments/11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.text").value("Комментарий 1 к посту 1"))
//                .andExpect(jsonPath("$.postId").value(11));
//
//        String json = """
//                  {"id":11,"text":"new comment text","postId":1}
//                """;
//
//        mockMvc.perform(put("/api/posts/11/comments/11")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(11))
//                .andExpect(jsonPath("$.text").value("new comment text"));
//    }
//
//    @Test
//    void deleteComment() throws Exception {
//        mockMvc.perform(get("/api/posts/11/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)));
//
//        mockMvc.perform(delete("/api/posts/11/comments/11"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/posts/11/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//}