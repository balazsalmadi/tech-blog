package com.blog.example.domain;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static org.hamcrest.Matchers.*;

public final class AnnotatedBookMatcher extends AnnotationBasedMatcher<Book> {

    @MatchAs( itemName = "title" )
    private Matcher<String> titleMatcher = new IsAnything<>();

    @MatchAs( itemName = "author" )
    private Matcher<String> authorMatcher = new IsAnything<>();

    @MatchAs( itemName = "publicationYear" )
    private Matcher<Integer> publicationYearMatcher = new IsAnything<>();

    @MatchAs( itemName = "opinions" )
    private Matcher<Iterable<? extends String>> opinionsMatcher = new IsAnything<>();

    private AnnotatedBookMatcher() {
    }

    public static AnnotatedBookMatcher anyBook() {
        return new AnnotatedBookMatcher();
    }

    public AnnotatedBookMatcher whichHasTitle( String expectedTitle ) {
        titleMatcher = equalTo( expectedTitle );
        return this;
    }

    public AnnotatedBookMatcher whichHasAuthor( String expectedAuthor ) {
        authorMatcher = equalTo( expectedAuthor );
        return this;
    }

    public AnnotatedBookMatcher whichHasPublicationYear( int expectedPublicationYear ) {
        publicationYearMatcher = equalTo( expectedPublicationYear );
        return this;
    }

    public AnnotatedBookMatcher whichHasPublicationYearAfter( int expectedPublicationYearAfter ) {
        publicationYearMatcher = greaterThan( expectedPublicationYearAfter );
        return this;
    }

    public AnnotatedBookMatcher whichHasOpinions( String... expectedOpinions ) {
        opinionsMatcher = describedAs( "[" + descriptionFormat( expectedOpinions ) + "] in any order", containsInAnyOrder( expectedOpinions ), expectedOpinions );
        return this;
    }

    private String descriptionFormat( String[] descriptionParts ) {
        List<String> elementIndicators = new ArrayList<>();
        for ( int i = 0; i < descriptionParts.length; i++ ) {
            elementIndicators.add( "%" + i );
        }
        return join( ", ", elementIndicators );
    }
}
