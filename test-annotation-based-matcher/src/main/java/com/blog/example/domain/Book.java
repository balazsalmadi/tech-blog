package com.blog.example.domain;

import java.util.List;

public class Book {

    private final String title;
    private final String author;
    private final int publicationYear;
    private final List<String> opinions;

    public Book( String title, String author, int publicationYear, List<String> opinions ) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.opinions = opinions;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public List<String> getOpinions() {
        return opinions;
    }
}
