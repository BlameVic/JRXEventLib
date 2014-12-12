package com.github.blamevic.event;

import java.util.ArrayList;
import java.util.List;

public class EventBus
{
    public List<IEventProcessor> processors = new ArrayList<>();

    public void processEvent(IEvent event)
    {
        for (IEventProcessor processor : processors)
        {
            for (IEventMatcher matcher : processor.getEventMatchers())
            {
                if (matcher.match(event))
                {
                    processor.processEvent(event);
                    break;
                }
            }
        }
    }
}
