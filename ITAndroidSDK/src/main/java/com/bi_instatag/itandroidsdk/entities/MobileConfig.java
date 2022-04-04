package com.bi_instatag.itandroidsdk.entities;

import com.bi_instatag.itandroidsdk.Environment;
import com.bi_instatag.itandroidsdk.ReportSuiteMapping;
import com.bi_instatag.itandroidsdk.ReportSuiteSetting;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileConfig {
    @SerializedName("web_property_name")
    String webPropertyName;

    public String getWebPropertyName() {
        return webPropertyName;
    }

    public void setWebPropertyName(String webPropertyName) {
        this.webPropertyName = webPropertyName;
    }

    @SerializedName("environment")
    String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @SerializedName("screens")
    List<Screen> screens;

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    /**
     * @param name
     * @return Screen | null
     */
    public Screen getScreen(String name) {
        for (Screen screen : screens) {
            if (screen.getName().equals(name)) {
                return screen;
            }
        }
        return null;
    }

    /**
     * @param screen
     * @return void
     */
    public void addScreen(Screen screen) {
        for (Screen existingScreen : screens) {
            if (screen.getName().equals(existingScreen.getName())) {
                return;
            }
        }
        screens.add(screen);
    }

    public String getReportSuite() {
        ReportSuiteSetting setting = ReportSuiteMapping.settings.get(webPropertyName);
        if (setting == null) {
            return "";
        }
        switch (environment) {
            case Environment.Staging:
                return setting.getDevURL();
            case Environment.Production:
                return setting.getProdURL();
            default:
                return "";
        }
    }
}
