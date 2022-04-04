package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class FormEndTrackingEvent extends TrackingEvent {

    private String formName;

    public FormEndTrackingEvent(Screen screen, String formName) {
        super(screen);
        this.formName = formName;
    }

    public FormEndTrackingEvent(String screenName, String formName) {
        super(screenName);
        this.formName = formName;
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        additionalContextData.put("eVar32", this.formName);
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event13");
        MobileCore.trackAction("event13", additionalContextData);
    }
}
