package com.github.blamevic.event.matchers;

import com.github.blamevic.event.IEvent;
import com.github.blamevic.event.IEventMatcher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ORMatcher implements IEventMatcher
{
    public List<IEventMatcher> matchers;
    public List<IEventMatcher> matchedMatchers;

    public ORMatcher(IEventMatcher... matchers)
    {
        this.matchers = Arrays.asList(matchers);
    }

    public ORMatcher(List<IEventMatcher> matchers)
    {
        this.matchers = matchers;
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
