package com.bi_instatag.itandroidsdk;

import android.app.Application;
import android.util.Log;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.UserProfile;
import com.bi_instatag.itandroidsdk.entities.MobileConfig;
import com.bi_instatag.itandroidsdk.entities.Screen;
import com.bi_instatag.itandroidsdk.events.ButtonClickTrackingEvent;
import com.bi_instatag.itandroidsdk.events.FormEndTrackingEvent;
import com.bi_instatag.itandroidsdk.events.FormStartTrackingEvent;
import com.bi_instatag.itandroidsdk.events.InputFieldChangeTrackingEvent;
import com.bi_instatag.itandroidsdk.events.LoginTrackingEvent;
import com.bi_instatag.itandroidsdk.events.RegistrationEndTrackingEvent;
import com.bi_instatag.itandroidsdk.events.RegistrationStartTrackingEvent;
import com.bi_instatag.itandroidsdk.events.ScreenViewTrackingEvent;
import com.bi_instatag.itandroidsdk.events.SearchTrackingEvent;
import com.bi_instatag.itandroidsdk.events.SoftAuthTrackingEvent;
import com.bi_instatag.itandroidsdk.events.TrackingEvent;
import com.bi_instatag.itandroidsdk.events.TrackingQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

enum State {
    NotReady,
    Ready,
    Error
}

public class Instatag implements InstatagAPIHandler {

    private static final String TAG = "Instatag";

    private static final String ADOBE_LAUNCH_ID_STAGING = "414cfb5a3140/1ea528a38d18/launch-11d2dca86545-staging";
    private static final String ADOBE_LAUNCH_ID_PRODUCTION = "414cfb5a3140/1ea528a38d18/launch-ca20917d654c";

    private static final String version = "v1.0.1";

    public static String getError() {
        return error;
    }

    private static String error;

    private static Instatag instance;

    public static LoggingMode mobileCoreLogLevel = LoggingMode.ERROR;

    private State instatagState = State.NotReady;
    private State adobeState = State.NotReady;

    private InstatagAPI api;

    private MobileConfig mobileConfig;

    private Application app;
    private String mobileKey;
    private String adobeLaunchId;

    private String screenName;

    private TrackingQueue trackingQueue = new TrackingQueue();
    private TrackingQueue unknownScreenQueue = new TrackingQueue();
    private TrackingQueue warmupQueue = new TrackingQueue();

    private Instatag() {}

    /**
     * Bootstrap Instatag instance and load application configuration from Instatag service and
     * initialize Adobe SDK.
     *
     * @param app Android Application instance
     * @param mobileKey Instatag mobile key for development or production environment
     * @throws InstatagException
     */
    public static void configure(Application app, String mobileKey) throws InstatagException {
        if (mobileKey == null || mobileKey.isEmpty()) {
            Log.e(TAG, "mobileKey must be set");
            throw new InstatagException("mobileKey must be set");
        }

        if (Instatag.instance == null) {
            Instatag.instance = new Instatag();
            Instatag.instance.app = app;
            Instatag.instance.mobileKey = mobileKey;
        }
        Instatag.instance.init();
    }

    public static String version() {
        return version;
    }

    private void init() throws InstatagException {
        api = new InstatagAPI(this);
        // First try to load application data
        try {
            api.loadMobileConfig(this.mobileKey);
        } catch (IOException e) {
            Log.e(TAG, "Failed to load application configuration");
            e.printStackTrace();
            throw new InstatagException("Failed to load application configuration", e);
        }
    }

    void handleWarmupQueue() {
        TrackingEvent event;

        do {
            event = this.warmupQueue.poll();
            addEventToQueue(event);
        } while (event != null);

        trackingQueue.execute();
    }

    public boolean isReady() {
        return instatagState == State.Ready && adobeState == State.Ready;
    }

    /*********************************************************************
     * Instatag API response handler
     */

    public void handleLoadMobileConfig(MobileConfig config) {
        mobileConfig = config;
        instatagState = State.Ready;

        String reportSuiteID = mobileConfig.getReportSuite();
        adobeLaunchId = mobileConfig.getEnvironment() == "staging" ? ADOBE_LAUNCH_ID_STAGING : ADOBE_LAUNCH_ID_PRODUCTION;

        // Initialize Adobe tracking suite
        try {
            MobileCore.setApplication(this.app);
            MobileCore.setLogLevel(Instatag.mobileCoreLogLevel);

            Analytics.registerExtension();
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            MobileCore.start(new AdobeCallback() {
                @Override
                public void call(Object o) {
                    MobileCore.configureWithAppID(adobeLaunchId);
                    MobileCore.lifecycleStart(null);

                    if (reportSuiteID != null && !reportSuiteID.isEmpty()) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("analytics.rsids", reportSuiteID);
                        MobileCore.updateConfiguration(data);
                    }

                    adobeState = State.Ready;
                    Log.i(TAG, "Adobe tracking initialized");

                    handleWarmupQueue();
                }
            });
        } catch (InvalidInitException e) {
            adobeState = State.Error;
            error = "Failed to initialize Adobe tracking suite";
            Log.e(TAG, error);
            e.printStackTrace();
        }
    }

    public void handleLoadMobileConfigFailed(String reason) {
        instatagState = State.Error;
        error = reason;
    }

    public void handleCreateScreen(Screen screen) {
        Log.i(TAG, "Adding new screen: " + screen.getName());
        // Append screen
        this.mobileConfig.addScreen(screen);

        // Update and transfer screen related events
        for (Iterator<TrackingEvent> iter = this.unknownScreenQueue.iterator(); iter.hasNext();) {
            TrackingEvent event = iter.next();
            if (event.getScreenName().equals(screen.getName())) {
                event.setScreen(screen);
                this.trackingQueue.add(event);
                iter.remove();
            }
        }

        this.trackingQueue.execute();
    }

    public void handleCreateScreenFailed(String reason) {
        instatagState = State.Error;
        error = reason;
    }

    /*********************************************************************
     * Instatag tracking facade implementation
     */

    public void addEventToQueue(TrackingEvent event) {
        Screen screen = mobileConfig.getScreen(event.getScreenName());

        if (isReady()) {
            if (screen == null) {
                // Create new screen
                unknownScreenQueue.add(event);
                Log.i(TAG, "Screen is unknown. Adding 'ScreenView' event to unknown screen queue");

                try {
                    api.createScreen(this.mobileKey, event.getScreenName());
                } catch (IOException e) {
                    String err = "Failed to create new screen: " + event.getScreenName();
                    Log.e(TAG, err);
                    e.printStackTrace();
                    error = err;
                }
            } else {
                event.setScreen(screen);
                trackingQueue.add(event);
            }
        } else {
            warmupQueue.add(event);
            Log.i(TAG, "Instatag initialization isn't finished yet. Adding 'ScreenView' event to warmup queue");
        }
    }

    /**
     * Track initial screen view
     *
     * @param screenName Identifier for current screen view
     */
    public static void trackScreen(String screenName) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackScreen because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        Instatag.instance.screenName = screenName;

        TrackingEvent event = new ScreenViewTrackingEvent(screenName);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track button click
     *
     * @param buttonName
     */
    public static void trackButton(String buttonName) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackButton because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackButton because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new ButtonClickTrackingEvent(Instatag.instance.screenName, buttonName);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track form start
     *
     * @param formName
     * @param speciality
     * @param medicalInformation
     */
    public static void trackFormStart(String formName, String speciality, String medicalInformation) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackFormStart because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackFormStart because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new FormStartTrackingEvent(Instatag.instance.screenName, formName, speciality, medicalInformation);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track form end
     *
     * @param formName
     * @param speciality
     * @param medicalInformation
     */
    public static void trackFormEnd(String formName, String speciality, String medicalInformation) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackFormEnd because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackFormEnd because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new FormEndTrackingEvent(Instatag.instance.screenName, formName, speciality, medicalInformation);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track search request
     *
     * @param keyword
     * @param results
     * @param category
     */
    public static void trackSearch(String keyword, String results, String category) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackSearch because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackSearch because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new SearchTrackingEvent(Instatag.instance.screenName, keyword, results, category);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track login
     *
     * @param id
     * @param authMethod
     * @param visitorType
     * @param idType
     */
    public static void trackLogin(String id, String authMethod, VisitorType visitorType, IDType idType) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackLogin because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackLogin because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new LoginTrackingEvent(Instatag.instance.screenName, id, authMethod, visitorType, idType);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track registration start
     *
     * @param formName
     * @param speciality
     * @param medicalInformation
     */
    public static void trackRegistrationFormStart(String formName, String speciality, String medicalInformation) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackRegistrationFormStart because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackRegistrationFormStart because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new RegistrationStartTrackingEvent(Instatag.instance.screenName, formName, speciality, medicalInformation);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track registration end
     *
     * @param formName
     * @param speciality
     * @param medicalInformation
     */
    public static void trackRegistrationFormEnd(String formName, String speciality, String medicalInformation) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackRegistrationFormEnd because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackRegistrationFormEnd because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new RegistrationEndTrackingEvent(Instatag.instance.screenName, formName, speciality, medicalInformation);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track input field change
     *
     * @param formName
     * @param fieldName
     */
    public static void trackInputFieldChange(String formName, String fieldName) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackInputFieldChange because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackInputFieldChange because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new InputFieldChangeTrackingEvent(Instatag.instance.screenName, formName, fieldName);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }

    /**
     * Track soft authentication
     *
     * @param visitorType
     */
    public static void trackSoftAuth(VisitorType visitorType) {
        if (Instatag.instance == null) {
            Log.e(TAG, "Can't execute #trackSoftAuth because Instatag isn't yet initialized");
            return; // TODO - Return smth or throw exception
        }
        if (Instatag.instance.screenName == null || Instatag.instance.screenName == "") {
            Log.e(TAG, "Can't execute #trackSoftAuth because screen name isn't set yet");
            return;
        }
        TrackingEvent event = new SoftAuthTrackingEvent(Instatag.instance.screenName, visitorType);

        Instatag.instance.addEventToQueue(event);
        Instatag.instance.trackingQueue.execute();
    }
}
