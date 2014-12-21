package com.github.blamevic.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventBus
{
    public List<IEventProcessor> processors;

    public EventBus()
    {
        this.processors = new ArrayList<>();
    }

    public EventBus(IEventProcessor... processors)
    {
        this.processors = Arrays.asList(processors);
    }

    public void processEvent(IEvent event)
    {
        for (IEventProcessor processor : processors)
            processEvent(event, processor);
    }

    public static void processEvent(IEvent event, IEventProcessor processor)
    {
        List<IEventMatcher> matchedMatchers = new ArrayList<>(processor.getEventMatchers().size());

        processor.getEventMatchers().parallelStream()
                .filter(matcher -> matcher.match(event))
                .forEach(matchedMatchers::add);

        processor.processEvent(event, matchedMatchers);
    }
}
