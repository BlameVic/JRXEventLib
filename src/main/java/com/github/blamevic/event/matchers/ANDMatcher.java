package com.github.blamevic.event.matchers;

import com.github.blamevic.event.IEvent;
import com.github.blamevic.event.IEventMatcher;

import java.util.Arrays;
import java.util.List;

public class ANDMatcher implements IEventMatcher
{
    public List<IEventMatcher> matchers;

    public ANDMatcher(IEventMatcher... matchers)
    {
        this.matchers = Arrays.asList(matchers);
    }

    public ANDMatcher(List<IEventMatcher> matchers)
    {
        this.matchers = matchers;
    }

    @Override
    public boolean match(IEvent event)
    {
        for (IEventMatcher matcher : this.matchers)
        {
            if (!matcher.match(event)) return false;
        }
        return true;
    }
}
