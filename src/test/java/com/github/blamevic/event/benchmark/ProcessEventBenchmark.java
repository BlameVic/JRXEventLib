package com.github.blamevic.event.benchmark;

import com.github.blamevic.event.EventBus;
import com.github.blamevic.event.IEvent;
import com.github.blamevic.event.IEventMatcher;
import com.github.blamevic.event.IEventProcessor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessEventBenchmark
{
    public static void main(String[] args)
    {
        int minProcessors = 1;
        int maxProcessors = 50;
        int processorsInterval = 1;

        int minMatchers = 1;
        int maxMatchers = 10;
        int matchersInterval = 1;

        int numEvents = 1_000_000;

        int repeats = 20;

        int runCount = 0;
        int totalRuns = (((maxProcessors - minProcessors) + 1) / processorsInterval) * (((maxMatchers - minMatchers) + 1) / matchersInterval);

        List<Result> results = new ArrayList<>(totalRuns);
        for (int numProcessors = minProcessors; numProcessors <= maxProcessors; numProcessors+= processorsInterval)
        {
            for (int numMatchers = minMatchers; numMatchers <= maxMatchers; numMatchers+= matchersInterval)
            {
                runCount++;
                System.out.println(runCount + "/" + totalRuns);

                Result[] aRepeats = new Result[repeats];

                for (int i = 0; i < repeats; i++)
                {
                    aRepeats[i] = runBenchmark(numEvents, numProcessors, numMatchers);
                }

                Result avg = Result.average(aRepeats);

                System.out.println("Average time: " + avg.timeMillis + "ms");

                results.add(avg);
            }
        }

        List<Integer> matcherRuns = new ArrayList<>();
        for (int numMatchers = minMatchers; numMatchers <= maxMatchers; numMatchers+= matchersInterval)
            matcherRuns.add(numMatchers);

        XYSeriesCollection collection = new XYSeriesCollection();

        for (int numMatchers : matcherRuns)
        {
            XYSeries series = new XYSeries("Matchers: " + numMatchers);
            for (Result result : results)
            {
                if (result.numMatchers == numMatchers)
                {
                    series.add(result.numProcessors, result.timeMillis);
                }
            }
            collection.addSeries(series);
        }


        JFreeChart chart = ChartFactory.createScatterPlot("Results: " + numEvents + " events", "numProcessors", "Time (ms)", collection);

        ChartFrame frame = new ChartFrame("", chart);
        frame.pack();
        frame.setVisible(true);
    }

    public static Result runBenchmark(int numEvents, int numProcessors, int numMatchers)
    {
        List<IEvent> events = new ArrayList<>(numEvents);
        for (int i = 0; i < numEvents; i++)
            events.add(new IEvent() {});

        List<IEventProcessor> processors = new ArrayList<>(numProcessors);
        for (int i = 0; i < numProcessors; i++)
            processors.add(new Processor(numMatchers));

        EventBus bus = new EventBus(processors.toArray(new IEventProcessor[processors.size()]));

        System.out.println("Benchmark Start with " + numEvents + " events, " + numProcessors + " processors, " + numMatchers + " matchers.");
        System.out.print("Total execution time: ");

        System.gc(); //Try to make sure it doesn't pause in the middle of a run

        long startTime = System.currentTimeMillis();

        for (IEvent event : events)
            bus.processEvent(event);

        long endTime = System.currentTimeMillis();

        long timeMillis = endTime - startTime;
        System.out.println(timeMillis + "ms");

        return new Result(numEvents, numProcessors, numMatchers, timeMillis);
    }

    public static class Result
    {
        public int numEvents, numProcessors, numMatchers;
        public long timeMillis;

        public Result(int numEvents, int numProcessors, int numMatchers, long timeMillis)
        {
            this.numEvents = numEvents;
            this.numProcessors = numProcessors;
            this.numMatchers = numMatchers;
            this.timeMillis = timeMillis;
        }

        public static Result average(Result... results)
        {
            int totalEvents = 0, totalProcessors = 0, totalMatchers = 0;
            long totalTimeMillis = 0;

            for (Result result : results)
            {
                totalEvents += result.numEvents;
                totalProcessors += result.numProcessors;
                totalMatchers += result.numMatchers;
                totalTimeMillis += result.timeMillis;
            }

            int len = results.length;
            return new Result(totalEvents / len, totalProcessors / len, totalMatchers / len, totalTimeMillis / len);
        }
    }

    public static class Processor implements IEventProcessor
    {
        List<IEventMatcher> matchers;

        public Processor(int numMatchers)
        {
            matchers =  new ArrayList<>(numMatchers);
            Random rand = new Random();

            for (int i = 0; i < numMatchers; i++)
            {
                if (rand.nextBoolean())
                    matchers.add(e -> true);
                else
                    matchers.add(e -> false);
            }
        }

        @Override
        public List<IEventMatcher> getEventMatchers()
        {
            return matchers;
        }

        @Override
        public void processEvent(IEvent event, List<IEventMatcher> matchedMatchers)
        {

        }
    }
}
