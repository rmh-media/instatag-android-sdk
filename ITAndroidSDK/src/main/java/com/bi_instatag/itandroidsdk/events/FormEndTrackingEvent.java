package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class FormEndTrackingEvent extends TrackingEvent {

    private String formName;
    private String speciality;
    private String medicalInformation;

    public FormEndTrackingEvent(Screen screen, String formName, String speciality, String medicalInformation) {
        super(screen);
        this.formName = formName;
        this.speciality = speciality == null ? "" : speciality;
        this.medicalInformation = medicalInformation == null ? "" : medicalInformation;
    }

    public FormEndTrackingEvent(String screenName, String formName, String speciality, String medicalInformation) {
        super(screenName);
        this.formName = formName;
        this.speciality = speciality == null ? "" : speciality;
        this.medicalInformation = medicalInformation == null ? "" : medicalInformation;
    }

    @Override
    public void fire() {
        if (this.screen == null) {
            return;
        }

        Map<String, String> additionalContextData = new HashMap<>();
        additionalContextData.put("eVar32", this.formName);
        additionalContextData.put("eVar33", this.medicalInformation);
        additionalContextData.put("eVar34", this.speciality);
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event13");
        MobileCore.trackAction("event13", additionalContextData);
    }
}
