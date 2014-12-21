package com.github.blamevic.event.matchers;

import com.github.blamevic.event.IEvent;
import com.github.blamevic.event.IEventMatcher;
import org.junit.Test;

import static org.junit.Assert.*;

public class ANDMatcherTest
{
    @Test
    public void testFalseFalse()
    {
        IEventMatcher matcher = new ANDMatcher(event -> false, event -> false);
        assertFalse("ANDMatcher should return false when both are false", matcher.match(new IEvent(){}));
    }

    @Test
    public void testFalseTrue()
    {
        IEventMatcher matcher = new ANDMatcher(event -> false, event -> true);
        assertFalse("ANDMatcher should return false when one is false", matcher.match(new IEvent(){}));
    }

    @Test
    public void testTrueTrue()
    {
        IEventMatcher matcher = new ANDMatcher(event -> true, event -> true);
        assertTrue("ANDMatcher should return true when both are true", matcher.match(new IEvent()
        {
        }));
    }

    @Test
    public void testSingle()
    {
        IEventMatcher matcher;

        matcher = new ANDMatcher(event -> true);
        assertTrue("ANDMatcher should return true when given one true", matcher.match(new IEvent(){}));

        matcher = new ANDMatcher(event -> false);
        assertFalse("ANDMatcher should return false when given one false", matcher.match(new IEvent(){}));
    }
}
