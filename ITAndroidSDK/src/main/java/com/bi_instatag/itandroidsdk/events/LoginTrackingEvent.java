package com.bi_instatag.itandroidsdk.events;

import com.adobe.marketing.mobile.MobileCore;
import com.bi_instatag.itandroidsdk.IDType;
import com.bi_instatag.itandroidsdk.VisitorType;
import com.bi_instatag.itandroidsdk.entities.Screen;

import java.util.HashMap;
import java.util.Map;

public class LoginTrackingEvent extends TrackingEvent {

    private String id;
    private String authMethod;
    private VisitorType visitorType;
    private IDType idType;

    public LoginTrackingEvent(Screen screen, String id, String authMethod, VisitorType visitorType, IDType idType) {
        super(screen);
        this.id = id;
        this.authMethod = authMethod;
        this.visitorType = visitorType;
        this.idType = idType;
    }

    public LoginTrackingEvent(String screenName, String id, String authMethod, VisitorType visitorType, IDType idType) {
        super(screenName);
        this.id = id;
        this.authMethod = authMethod;
        this.visitorType = visitorType;
        this.idType = idType;
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

    private String getIdType() {
        switch (this.idType) {
            case VeevaId:
                return "VeevaId";
            case Unknown:
                return (this.id.length() == 15 || this.id.length() == 18) ? "VeevaID" : "";
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
        additionalContextData.put("eVar18", this.id);
        additionalContextData.put("eVar67", this.authMethod);
        additionalContextData.put("eVar78", this.getVisitorType());
        additionalContextData.put("eVar112", this.getIdType());
        additionalContextData.put("mid", "D=mid");
        additionalContextData.put("&&events", "event2");
        MobileCore.trackAction("event2", additionalContextData);
        System.out.println("Instatag: executed trackAction 'visitor_loginComplete' (eVar18:" + this.id + ", eVar67:" + this.authMethod + ", eVar78:" + this.getVisitorType() + ", eVar112:" + this.getIdType() + ")");
    }
}
