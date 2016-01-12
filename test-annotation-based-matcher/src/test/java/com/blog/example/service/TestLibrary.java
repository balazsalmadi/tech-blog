package com.blog.example.service;

import com.blog.example.domain.Book;
import org.junit.Test;

import static com.blog.example.domain.AnnotatedBookMatcher.anyBook;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestLibrary {

    @Test
    public void testFailToMatchTitleAndAuthor() throws Exception {
        Library library = new Library();

        Book book = library.findByTitle( "Lord of the Rings" );

        assertThat( book, anyBook().whichHasTitle( "Lord of the Rings" )
                                   .whichHasAuthor( "J. R. R." ) );
    }

    @Test
    public void testFailToMatchOpinions() throws Exception {
        Library library = new Library();

        Book book = library.findByTitle( "Insomnia" );

        assertThat( book, anyBook().whichHasOpinions( "Great book!", "Worth to " ) );
    }

    @Test
    public void testFailToMatchBook() throws Exception {
        Library library = new Library();

        Book book = library.findByTitle( "Lord of the Rings" );

        assertThat( book, anyBook().whichHasTitle( "Lord of the" )
                                   .whichHasAuthor( "J. R. R. " )
                                   .whichHasPublicationYearAfter( 2000 )
                                   .whichHasOpinions( "liked" ) );
    }

    @Test
    public void testBookPartialMatches() throws Exception {
        Library library = new Library();

        Book book = library.findByTitle( "Lord of the Rings" );

        assertThat( book, anyBook().whichHasTitle( "Lord of the Rings" )
                                   .whichHasAuthor( "J. R. R. " )
                                   .whichHasPublicationYearAfter( 1950 )
                                   .whichHasOpinions( "liked" ) );
    }
}
