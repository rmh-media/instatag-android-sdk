package com.bi_instatag.itandroidsdk.events;

import com.bi_instatag.itandroidsdk.entities.Screen;

public abstract class TrackingEvent {

    protected TrackingEvent(Screen screen) {
        this.screen = screen;
    }

    protected TrackingEvent(String screenName) {
        this.screenName = screenName;
    }

    protected String screenName;

    public void setScreenName(String screenName) {
        // TODO - Handle case when screen is also set
        this.screenName = screenName;
    }

    public String getScreenName() {
        if (screen != null) {
            return screen.getName();
        }
        return screenName;
    }

    protected Screen screen;

    public void setScreen(Screen screen) {
        this.screen = screen;
        this.screenName = screen.getName();
    }

    public Screen getScreen() {
        return screen;
    }

    public abstract void fire();
}
