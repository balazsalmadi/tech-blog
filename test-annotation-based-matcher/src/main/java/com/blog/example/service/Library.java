package com.blog.example.service;

import com.blog.example.domain.Book;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;


public class Library {
    private List<Book> books = asList(
            new Book( "Lord of the Rings", "J. R. R. Tolkien", 1954, asList( "I liked it." ) ),
            new Book( "Harry Potter and the Philosopher's Stone", "J. K. Rowling", 1997, asList( "My child love it." ) ),
            new Book( "Insomnia", "Stephen King", 1994, asList( "Great book!", "Worth to read it!" ) )
    );

    public Book findByTitle( String title ) {
        return books.stream()
                    .filter( book -> book.getTitle().equals( title ) ).findFirst()
                    .orElseThrow( NoSuchElementException::new );
    }
}
