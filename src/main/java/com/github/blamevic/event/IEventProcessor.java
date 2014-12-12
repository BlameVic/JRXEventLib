package com.github.blamevic.event;

import java.util.List;

public interface IEventProcessor
{
    public List<IEventMatcher> getEventMatchers();

    public void processEvent(IEvent event);
}
