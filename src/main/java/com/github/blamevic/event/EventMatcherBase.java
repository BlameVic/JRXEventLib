package com.github.blamevic.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EventMatcherBase implements IEventMatcher
{
    List<IEventMatcher> matchers = new ArrayList<>();

    protected void addEventMatcher(IEventMatcher... eventMatchers)
    {
        matchers.addAll(Arrays.asList(eventMatchers));
    }

    @Override
    public final boolean match(IEvent event)
    {
        for (IEventMatcher matcher : matchers)
        {
            if (!matcher.match(event)) return false;
        }
        return subMatch(event);
    }

    public abstract boolean subMatch(IEvent event);
}
