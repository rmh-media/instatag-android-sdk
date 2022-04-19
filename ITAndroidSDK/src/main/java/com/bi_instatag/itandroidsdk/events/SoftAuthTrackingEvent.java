package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.VisitorType;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class SoftAuthTrackingEvent extends TrackingEvent {

    private VisitorType visitorType;

    public SoftAuthTrackingEvent(Screen screen, VisitorType visitorType) {
        super(screen);
        this.visitorType = visitorType;
    }

    public SoftAuthTrackingEvent(String screenName, VisitorType visitorType) {
        super(screenName);
        this.visitorType = visitorType;
    }

    private String getVisitorType() {
        switch (this.visitorType) {
            case HCP:
                return "HCP";
            case Rep:
                return "Rep";
            default:
                return "";
        }
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        additionalContextData.put("eVar78", this.getVisitorType());
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event95");

        MobileCore.trackAction("event95", additionalContextData);
    }
}
