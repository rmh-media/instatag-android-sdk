package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class SearchTrackingEvent extends TrackingEvent {

    private String keyword;
    private String results;

    public SearchTrackingEvent(Screen screen, String keyword, String results) {
        super(screen);
        this.keyword = keyword;
        this.results = results;
    }

    public SearchTrackingEvent(String screenName, String keyword, String results) {
        super(screenName);
        this.keyword = keyword;
        this.results = results;
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        additionalContextData.put("eVar21", this.keyword);
        additionalContextData.put("eVar22", this.results);
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event3");
        MobileCore.trackAction("event3", additionalContextData);
    }
}
