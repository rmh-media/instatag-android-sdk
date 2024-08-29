package com.bi_instatag.itandroidsdk;

import org.junit.Test; // Import JUnit Test annotation

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReportSuiteTest {
    @Test
    public void should_Return_Empty_String_When_both_setting_is_null() {
        ReportSuite reportSuite = new ReportSuite(null, null);
        assertEquals("", reportSuite.getDevURL());
        assertEquals("", reportSuite.getProdURL());
    }

    @Test
    public void should_Return_Empty_String_When_both_setting_urls_are_empty() {
        ReportSuiteSetting globalSetting = new ReportSuiteSetting("GLOBAL", "", "");
        ReportSuiteSetting setting = new ReportSuiteSetting("SETTING", "", "");

        ReportSuite reportSuite = new ReportSuite(setting, globalSetting);
        assertEquals("", reportSuite.getDevURL());
        assertEquals("", reportSuite.getProdURL());
    }

    @Test
    public void should_Return_only_global_setting_url_When_setting_is_null() {
        ReportSuiteSetting globalSetting = new ReportSuiteSetting("GLOBAL", "global.dev", "global.prod");
        ReportSuite reportSuite = new ReportSuite(null, globalSetting);
        assertEquals("global.dev", reportSuite.getDevURL());
        assertEquals("global.prod", reportSuite.getProdURL());
    }

    @Test
    public void should_Return_only_global_setting_url_When_setting_urls_are_empty() {
        ReportSuiteSetting globalSetting = new ReportSuiteSetting("GLOBAL", "global.dev", "global.prod");
        ReportSuiteSetting setting = new ReportSuiteSetting("SETTING", "", "");
        ReportSuite reportSuite = new ReportSuite(setting, globalSetting);
        assertEquals("global.dev", reportSuite.getDevURL());
        assertEquals("global.prod", reportSuite.getProdURL());
    }

    @Test
    public void should_Return_only_setting_url_When_global_setting_is_null() {
        ReportSuiteSetting setting = new ReportSuiteSetting("SETTING", "setting.dev", "setting.prod");
        ReportSuite reportSuite = new ReportSuite(setting, null);
        assertEquals("setting.dev", reportSuite.getDevURL());
        assertEquals("setting.prod", reportSuite.getProdURL());
    }

    @Test
    public void should_Return_only_setting_url_When_global_setting_urls_are_empty() {
        ReportSuiteSetting setting = new ReportSuiteSetting("SETTING", "setting.dev", "setting.prod");
        ReportSuiteSetting globalSetting = new ReportSuiteSetting("GLOBAL", "", "");

        ReportSuite reportSuite = new ReportSuite(setting, globalSetting);
        assertEquals("setting.dev", reportSuite.getDevURL());
        assertEquals("setting.prod", reportSuite.getProdURL());
    }

    @Test
    public void should_Return_combined_urls_When_global_setting_and_setting_are_not_null() {
        ReportSuiteSetting setting = new ReportSuiteSetting("SETTING", "setting.dev", "setting.prod");
        ReportSuiteSetting globalSetting = new ReportSuiteSetting("GLOBAL", "global.dev", "global.prod");

        ReportSuite reportSuite = new ReportSuite(setting, globalSetting);
        assertEquals("setting.dev,global.dev", reportSuite.getDevURL());
        assertEquals("setting.prod,global.prod", reportSuite.getProdURL());
    }
}