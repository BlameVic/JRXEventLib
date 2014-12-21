package com.github.blamevic.event.matchers;

import com.github.blamevic.event.IEvent;
import com.github.blamevic.event.IEventMatcher;
import org.junit.Test;

import static org.junit.Assert.*;

public class ORMatcherTest
{
    @Test
    public void testFalseFalse()
    {
        IEventMatcher matcher = new ORMatcher(event -> false, event -> false);
        assertFalse("ORMatcher should return false when both are false", matcher.match(new IEvent(){}));
    }

    @Test
    public void testFalseTrue()
    {
        IEventMatcher matcher = new ORMatcher(event -> false, event -> true);
        assertTrue("ORMatcher should return true when one is true", matcher.match(new IEvent(){}));
    }

    @Test
    public void testTrueTrue()
    {
        IEventMatcher matcher = new ORMatcher(event -> true, event -> true);
        assertTrue("ORMatcher should return true when both are true", matcher.match(new IEvent()
        {
        }));
    }

    @Test
    public void testSingle()
    {
        IEventMatcher matcher;

        matcher = new ORMatcher(event -> true);
        assertTrue("ORMatcher should return true when given one true", matcher.match(new IEvent(){}));

        matcher = new ORMatcher(event -> false);
        assertFalse("ORMatcher should return false when given one false", matcher.match(new IEvent(){}));
    }
}
