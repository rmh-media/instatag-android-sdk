package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.EVar;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class ButtonClickTrackingEvent extends TrackingEvent {

    private String buttonName;

    public ButtonClickTrackingEvent(Screen screen, String buttonName) {
        super(screen);
        this.buttonName = buttonName;
    }

    public ButtonClickTrackingEvent(String screenName, String buttonName) {
        super(screenName);
        this.buttonName = buttonName;
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        additionalContextData.put("eVar28", this.buttonName);
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event29");
        MobileCore.trackAction("event29", additionalContextData);
    }
}
