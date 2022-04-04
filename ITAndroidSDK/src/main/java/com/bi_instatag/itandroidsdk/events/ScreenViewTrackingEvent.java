package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.EVar;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class ScreenViewTrackingEvent extends TrackingEvent {

    public ScreenViewTrackingEvent(Screen screen) {
        super(screen);
    }

    public ScreenViewTrackingEvent(String screenName) {
        super(screenName);
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        for (EVar evar : this.screen.getEvars()) {
            additionalContextData.put(evar.name, evar.value);
        }
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event1");
        MobileCore.trackState(this.screen.getName(), additionalContextData);
    }
}
