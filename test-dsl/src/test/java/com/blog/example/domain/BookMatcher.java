package com.blog.example.domain;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class BookMatcher extends TypeSafeDiagnosingMatcher<Book> {

    private Matcher<String> titleMatcher;

    private Matcher<String> authorMatcher;

    private Matcher<Integer> publicationYearMatcher;

    private BookMatcher(Matcher<String> titleMatcher, Matcher<String> authorMatcher, Matcher<Integer> publicationYearMatcher) {
        this.titleMatcher = titleMatcher;
        this.authorMatcher = authorMatcher;
        this.publicationYearMatcher = publicationYearMatcher;
    }

    public static BookMatcher aBookWhichHas(String title, String author, int publicationYear) {
        return new BookMatcher(equalTo(title), equalTo(author), equalTo(publicationYear));
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
