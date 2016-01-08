package com.blog.example.domain;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsAnything;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class DeclarativeBookMatcher extends TypeSafeDiagnosingMatcher<Book> {

    private Matcher<String> titleMatcher = new IsAnything<>();

    private Matcher<String> authorMatcher = new IsAnything<>();

    private Matcher<Integer> publicationYearMatcher = new IsAnything<>();

    private DeclarativeBookMatcher() {
    }

    public static DeclarativeBookMatcher anyBook() {
        return new DeclarativeBookMatcher();
    }

    public DeclarativeBookMatcher whichHasTitle(String expectedTitle) {
        titleMatcher = equalTo(expectedTitle);
        return this;
    }

    public DeclarativeBookMatcher whichHasAuthor(String expectedAuthor) {
        authorMatcher = equalTo(expectedAuthor);
        return this;
    }

    public DeclarativeBookMatcher whichHasPublicationYear(int expectedPublicationYear) {
        publicationYearMatcher = equalTo(expectedPublicationYear);
        return this;
    }

    @Override
    protected boolean matchesSafely(Book item, Description mismatchDescription) {
        List<String> mismatchMessages = new ArrayList<>(3);

        boolean bookMatches = isMatch(titleMatcher, item.getTitle(), "title", mismatchMessages);
        bookMatches = isMatch(authorMatcher, item.getAuthor(), "author", mismatchMessages) && bookMatches;
        bookMatches = isMatch(publicationYearMatcher, item.getPublicationYear(), "publicationYear", mismatchMessages) && bookMatches;

        mismatchDescription.appendText(String.join(", and ", mismatchMessages));
        return bookMatches;
    }

    private <K> boolean isMatch(Matcher<K> matcher, K item, String itemName, List<String> mismatchMessages) {
        if (!matcher.matches(item)) {
            mismatchMessages.add(itemName + " was " + item);
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Book which")
                   .appendText(" has title ").appendDescriptionOf(titleMatcher)
                   .appendText(", and has author ").appendDescriptionOf(authorMatcher)
                   .appendText(", and has publicationYear ").appendDescriptionOf(publicationYearMatcher);
    }
}
