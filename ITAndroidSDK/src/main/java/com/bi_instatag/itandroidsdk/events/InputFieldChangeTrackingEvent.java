package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class InputFieldChangeTrackingEvent extends TrackingEvent {

    private String formName;
    private String inputName;

    public InputFieldChangeTrackingEvent(Screen screen, String formName, String inputName) {
        super(screen);
        this.formName = formName;
        this.inputName = inputName;
    }

    public InputFieldChangeTrackingEvent(String screenName, String formName, String inputName) {
        super(screenName);
        this.formName = formName;
        this.inputName = inputName;
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        additionalContextData.put("eVar32", this.formName);
        additionalContextData.put("eVar115", this.inputName);
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event112");

        MobileCore.trackAction("event112", additionalContextData);
    }
}
