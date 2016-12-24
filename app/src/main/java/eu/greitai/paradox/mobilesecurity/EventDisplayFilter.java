package eu.greitai.paradox.mobilesecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.greitai.paradox.mobilesecurity.data.SecurityEvent;

public class EventDisplayFilter {
    private static final Map<Integer, List<Integer>> noisyEvents;
    static
    {
        noisyEvents = new HashMap<>();
        // these are just noise, which we do not want to display
        // check string_events for their meaning
        noisyEvents.put(2, Arrays.asList(new Integer[]{8,9}));
        noisyEvents.put(3, Arrays.asList(new Integer[]{2,3}));
    }


    private static boolean isNoisyEvent(SecurityEvent item) {
        List<Integer> events = noisyEvents.get(Integer.valueOf(item.getEventGroup()));
        return events != null && events.contains(Integer.valueOf(item.getEvent()));
    }

    public static List<SecurityEvent> removeNoisyEvents(List<SecurityEvent> events) {
        List<SecurityEvent> filtered = new ArrayList<>(events.size());
        for (SecurityEvent event:  events) {
            if(!isNoisyEvent(event)) {
                filtered.add(event);
            }
        }
        return filtered;
    }
}
