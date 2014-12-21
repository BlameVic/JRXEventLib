package com.github.blamevic.event;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EventBusTest
{
    public boolean testDeliveryHasRun;

    @Test
    public void testDelivery()
    {

        EventBus bus = new EventBus();
        bus.processors.add(new IEventProcessor()
        {
            @Override
            public List<IEventMatcher> getEventMatchers()
            {
                return Arrays.asList(event -> true);
            }

            @Override
            public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
            {
                testDeliveryHasRun = true;
            }
        });

        bus.processEvent(new IEvent(){});

        assertTrue("Events should be delivered", testDeliveryHasRun);
    }

    public boolean testNonDeliveryHasRun = false;

    @Test
    public void testNonDelivery()
    {

        EventBus bus = new EventBus();
        bus.processors.add(new IEventProcessor()
        {
            @Override
            public List<IEventMatcher> getEventMatchers()
            {
                return Arrays.asList(event -> false);
            }

            @Override
            public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
            {
                testNonDeliveryHasRun = true;
            }
        });

        bus.processEvent(new IEvent(){});

        assertFalse("Events should not be delivered if no matchers match", testNonDeliveryHasRun);
    }
}
