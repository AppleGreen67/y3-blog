package ru.ythree.blog.model;

public class Image {
    private String name;
    private byte[] data;
    private Long postId;

    public Image() {
    }

    public Image(String name, byte[] data, Long postId) {
        this.name = name;
        this.data = data;
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
