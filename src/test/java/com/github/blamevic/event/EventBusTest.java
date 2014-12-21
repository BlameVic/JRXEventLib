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

    public boolean testMultipleMatchersHasRun = false;

    @Test
    public void testMultipleMatchers()
    {

        EventBus bus = new EventBus();
        bus.processors.add(new IEventProcessor()
        {
            @Override
            public List<IEventMatcher> getEventMatchers()
            {
                return Arrays.asList(event -> false, event -> true);
            }

            @Override
            public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
            {
                testMultipleMatchersHasRun = true;
            }
        });

        bus.processEvent(new IEvent(){});

        assertTrue("Events should be delivered if just one matcher is true", testMultipleMatchersHasRun);
    }

    public boolean testMultipleProcessorsHasRun = false;
    public boolean testMultipleProcessorsHasRun2 = false;
    public boolean testMultipleProcessorsHasRun3 = false;

    @Test
    public void testMultipleProcessors()
    {
        EventBus bus = new EventBus();

        bus.processors.add(new IEventProcessor()
        {
            @Override
            public List<IEventMatcher> getEventMatchers()
            {
                return Arrays.asList(event -> true, event -> true);
            }

            @Override
            public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
            {
                testMultipleProcessorsHasRun = true;
            }
        });

        bus.processors.add(new IEventProcessor()
        {
            @Override
            public List<IEventMatcher> getEventMatchers()
            {
                return Arrays.asList(event -> true, event -> false);
            }

            @Override
            public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
            {
                testMultipleProcessorsHasRun2 = true;
            }
        });

        bus.processors.add(new IEventProcessor()
        {
            @Override
            public List<IEventMatcher> getEventMatchers()
            {
                return Arrays.asList(event -> false, event -> false);
            }

            @Override
            public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
            {
                testMultipleProcessorsHasRun3 = true;
            }
        });

        bus.processEvent(new IEvent(){});

        assertTrue("Having multiple processors should be supported", testMultipleProcessorsHasRun && testMultipleProcessorsHasRun2 && !testMultipleProcessorsHasRun3);
    }
}
