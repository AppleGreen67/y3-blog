package ru.ythree.blog.model;

import java.util.List;

public class Page {
    private List<Post> posts;
    private boolean hasPrev; //true, если текущая страница не первая
    private boolean hasNext; //true, если текущая страница не последняя
    private int lastPage;//номер последней страницы

    public Page() {
    }

    public Page(List<Post> posts, boolean hasPrev, boolean hasNext, int lastPage) {
        this.posts = posts;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
        this.lastPage = lastPage;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "posts=" + posts +
                ", hasPrev=" + hasPrev +
                ", hasNext=" + hasNext +
                ", lastPage=" + lastPage +
                '}';
    }
}