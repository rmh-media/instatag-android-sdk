package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class SearchTrackingEvent extends TrackingEvent {

    private String keyword;
    private String results;
    private String category;

    public SearchTrackingEvent(Screen screen, String keyword, String results, String category) {
        super(screen);
        this.keyword = keyword;
        this.results = results;
        this.category = category == null ? "" : category;
    }

    public SearchTrackingEvent(String screenName, String keyword, String results, String category) {
        super(screenName);
        this.keyword = keyword;
        this.results = results;
        this.category = category == null ? "" : category;
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

        if (this.category != "") {
            additionalContextData.put("evar86", this.category);
        }

        MobileCore.trackAction("event3", additionalContextData);
    }
}
