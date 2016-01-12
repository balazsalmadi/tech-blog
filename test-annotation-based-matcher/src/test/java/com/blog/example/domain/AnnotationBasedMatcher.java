package com.blog.example.domain;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsAnything;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AnnotationBasedMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    private String itemName = "";
    private Map<String, Matcher> fieldMatchers = new HashMap<>();

    @Override
    protected boolean matchesSafely( T item, Description mismatchDescription ) {
        itemName = item.getClass().getSimpleName();
        Field[] fields = this.getClass().getDeclaredFields();
        List<String> mismatches = new ArrayList<>();
        for ( Field field : fields ) {
            if ( Matcher.class.isAssignableFrom( field.getType() ) ) {
                MatchAs matchAs = field.getAnnotation( MatchAs.class );
                field.setAccessible( true );
                Matcher matcher = convertToMatcher( field );
                fieldMatchers.put( matchAs.itemName(), matcher );
                Object fieldValue = fieldValue( item, getterMethod( item, matchAs ) );
                if ( !matcher.matches( fieldValue ) ) {
                    Description mismatch = new StringDescription();
                    mismatch.appendText( matchAs.itemName() ).appendText( " was " ).appendValue( fieldValue );
                    mismatches.add( mismatch.toString() );
                }
            }
        }

        mismatchDescription.appendText( String.join( ", and ", mismatches ) );

        return mismatches.isEmpty();
    }

    private Object fieldValue( T item, Method getter ) {
        try {
            return getter.invoke( item );
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException( e );
        }
    }

    private Method getterMethod( T item, MatchAs annotation ) {
        String getterName = Optional.ofNullable( annotation.getter() )
                                    .filter( getter -> !getter.isEmpty() )
                                    .orElse( getterName( annotation.itemName() ) );
        try {
            return item.getClass().getMethod( getterName );
        } catch ( NoSuchMethodException e ) {
            throw new RuntimeException( e );
        }
    }

    private String getterName( String itemName ) {
        return "get" + convertFirstLetterToUpperCase( itemName );
    }

    private String convertFirstLetterToUpperCase( String itemName ) {
        return itemName.substring( 0, 1 ).toUpperCase() + itemName.substring( 1 );
    }

    private Matcher convertToMatcher( Field field ) {
        try {
            return ( Matcher ) field.get( this );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void describeTo( Description description ) {
        List<String> descriptionParts = new ArrayList<>();
        for ( Map.Entry<String, Matcher> fieldMatchersEntry : fieldMatchers.entrySet() ) {
            if ( !fieldMatchersEntry.getValue().getClass().isAssignableFrom( IsAnything.class ) ) {
                StringDescription matcherDescription = new StringDescription();
                fieldMatchersEntry.getValue().describeTo( matcherDescription );
                descriptionParts.add( " has " + fieldMatchersEntry.getKey() + " " + matcherDescription.toString() );
            }
        }
        description.appendText( itemName ).appendText( " which" ).appendText( String.join( ", and", descriptionParts ) );
    }
}
