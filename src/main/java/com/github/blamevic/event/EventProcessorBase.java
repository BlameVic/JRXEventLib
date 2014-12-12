package com.github.blamevic.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EventProcessorBase implements IEventProcessor
{
    List<IEventMatcher> matchers = new ArrayList<>();

    @Override
    public List<IEventMatcher> getEventMatchers()
    {
        return matchers;
    }

    protected void addEventMatcher(IEventMatcher... eventMatchers)
    {
        matchers.addAll(Arrays.asList(eventMatchers));
    }
}
