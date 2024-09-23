package com.bi_instatag.itandroidsdk;

import com.bi_instatag.itandroidsdk.ReportSuiteSetting;

public class ReportSuite {
    ReportSuiteSetting setting;
    ReportSuiteSetting globalSetting;
    String REPORT_SUITE_EMPTY = "EMPTY";

    public ReportSuite(ReportSuiteSetting setting, ReportSuiteSetting globalSetting) {
        this.setting = prepareReportSuiteSetting(setting);
        this.globalSetting = prepareReportSuiteSetting(globalSetting);
    }

    public String getProdURL() {
        String settingURL = this.setting.getProdURL();
        String globalUrl = this.globalSetting.getProdURL();

        return this.getURL(settingURL, globalUrl);
    } 
    
    public String getDevURL() {
        String settingURL = this.setting.getDevURL();
        String globalUrl = this.globalSetting.getDevURL();

        return this.getURL(settingURL, globalUrl);
    } 

    private ReportSuiteSetting prepareReportSuiteSetting(ReportSuiteSetting setting) {
        if (setting == null) {
            return new ReportSuiteSetting(REPORT_SUITE_EMPTY, "", "");
        }
        
        return setting;
    }

    private String getURL(String settingUrl, String globalUrl) {
        String url = settingUrl + ',' + globalUrl;
        return url.replaceAll("^,+|,+$", "");
    }
}