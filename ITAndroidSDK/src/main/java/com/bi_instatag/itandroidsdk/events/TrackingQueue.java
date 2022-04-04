package com.bi_instatag.itandroidsdk.events;

import java.util.LinkedList;

public class TrackingQueue extends LinkedList<TrackingEvent> {

    public void execute() {
        TrackingEvent event;

        do {
            event = poll();
            if (event != null) {
                event.fire();
            }
        } while (event != null);
    }

}
