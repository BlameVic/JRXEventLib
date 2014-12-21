package com.github.blamevic.event.matchers;

import com.github.blamevic.event.IEvent;
import com.github.blamevic.event.IEventMatcher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ORMatcher implements IEventMatcher
{
    List<IEventMatcher> matchers;
    List<IEventMatcher> matchedMatchers = null;

    public ORMatcher(IEventMatcher... matchers)
    {
        this.matchers = Arrays.asList(matchers);
    }

    @Override
    public boolean match(IEvent event)
    {
        matchedMatchers = new LinkedList<>();

        for (IEventMatcher matcher : matchers)
            if (matcher.match(event))
                matchedMatchers.add(matcher);

        return matchedMatchers.size() != 0;
    }
}
