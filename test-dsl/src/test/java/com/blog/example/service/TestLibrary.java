package com.blog.example.service;

import com.blog.example.domain.Book;
import org.junit.Test;

import static com.blog.example.domain.BookMatcher.aBookWhichHas;
import static com.blog.example.domain.DeclarativeBookMatcher.anyBook;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class TestLibrary {

    @Test
    public void testWithoutMatcher() throws Exception {
        Library library = new Library();
        Book book = library.findByTitle("Lord of the Rings");

        assertEquals("Lord of the Rings", book.getTitle());
        assertEquals("J. R. R. Tolkien", book.getAuthor());
        assertEquals(1954, book.getPublicationYear());
    }

    @Test
    public void testFailsWithoutMatcher() throws Exception {
        Library library = new Library();
        Book book = library.findByTitle("Lord of the Rings");

        assertEquals("Lord of the", book.getTitle());
        assertEquals("Tolkien", book.getAuthor());
        assertEquals(2000, book.getPublicationYear());
    }

    @Test
    public void testWithDefaultMatchers() throws Exception {
        Library library = new Library();
        Book book = library.findByTitle("Lord of the Rings");

        assertThat(book.getTitle(), equalTo("Lord of the Rings"));
        assertThat(book.getAuthor(), equalTo("J. R. R. Tolkien"));
        assertThat(book.getPublicationYear(), equalTo(1954));
    }

    @Test
    public void testFailsWithBookMatcher() throws Exception {
        Library library = new Library();
        Book book = library.findByTitle("Lord of the Rings");

        assertThat(book, aBookWhichHas("Lord of the", "Tolkien", 2000));
    }

    @Test
    public void testFailsWithDeclarativeBookMatcher() throws Exception {
        Library library = new Library();
        Book book = library.findByTitle("Lord of the Rings");

        assertThat(book, anyBook().whichHasTitle("Lord of the")
                                  .whichHasAuthor("Tolkien")
                                  .whichHasPublicationYear(2000));
    }
}
