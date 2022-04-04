package com.bi_instatag.itandroidsdk;

import java.util.HashMap;

public class ReportSuiteMapping {

    public static final HashMap<String, ReportSuiteSetting> settings;
    static {
        settings = new HashMap<String, ReportSuiteSetting>();
        settings.put("AH Europe", new ReportSuiteSetting("AH Europe", "boehr.ah.eu.app-dev", "boehr.ah.eu.app-prod"));
        settings.put("AH Latin America", new ReportSuiteSetting("AH Latin America", "boehr.ah.latam.app-dev", "boehr.ah.latam.app-prod"));
        settings.put("AH Met Asia", new ReportSuiteSetting("AH Met Asia", "boehr.ah.metasia.app-dev", "boehr.ah.metasia.app-prod"));
        settings.put("AH North America", new ReportSuiteSetting("AH North America", "boehr.ah.na.app-dev", "boehr.ah.na.app-prod"));
        settings.put("Corporate", new ReportSuiteSetting("Corporate", "boehr.corporate.app-dev", "boehr.corporate.app-prod"));
        settings.put("Internal", new ReportSuiteSetting("Internal", "boehr.showcase.app-dev", "boehr.showcase.app-prod"));
        settings.put("Australia", new ReportSuiteSetting("Australia", "boehr.au.app-dev", "boehr.au.app-prod"));
        settings.put("Brazil", new ReportSuiteSetting("Brazil", "boehr.br.app-dev", "boehr.br.app-prod"));
        settings.put("Canada", new ReportSuiteSetting("Canada", "boehr.ca.app-dev", "boehr.ca.app-prod"));
        settings.put("China", new ReportSuiteSetting("China", "boehr.cn.app-dev", "boehr.cn.app-prod"));
        settings.put("France", new ReportSuiteSetting("France", "boehr.fr.app-dev", "boehr.fr.app-prod"));
        settings.put("Germany", new ReportSuiteSetting("Germany", "boehr.de.app-dev", "boehr.de.app-prod"));
        settings.put("India", new ReportSuiteSetting("India", "boehr.in.app-dev", "boehr.in.app-prod"));
        settings.put("Italy", new ReportSuiteSetting("Italy", "boehr.it.app-dev", "boehr.it.app-prod"));
        settings.put("Japan", new ReportSuiteSetting("Japan", "boehr.jp.app-dev", "boehr.jp.app-prod"));
        settings.put("META", new ReportSuiteSetting("META", "boehr.meta.app-dev", "boehr.meta.app-prod"));
        settings.put("MIDI", new ReportSuiteSetting("MIDI", "boehr.midi.app-dev", "boehr.midi.app-prod"));
        settings.put("Mexico", new ReportSuiteSetting("Mexico", "boehr.mx.app-dev", "boehr.mx.app-prod"));
        settings.put("RCV", new ReportSuiteSetting("RCV", "boehr.rcv.app-dev", "boehr.rcv.app-prod"));
        settings.put("SEASK", new ReportSuiteSetting("SEASK", "boehr.seask.app-dev", "boehr.seask.app-prod"));
        settings.put("South America", new ReportSuiteSetting("South America", "boehr.sam.app-dev", "boehr.sam.app-prod"));
        settings.put("Spain", new ReportSuiteSetting("Spain", "boehr.es.app-dev", "boehr.es.app-prod"));
        settings.put("TCM", new ReportSuiteSetting("TCM", "boehr.tcm.app-dev", "boehr.tcm.app-prod"));
        settings.put("UK", new ReportSuiteSetting("UK", "boehr.uk.app-dev", "boehr.uk.app-prod"));
        settings.put("US", new ReportSuiteSetting("US", "boehr.us.app-dev", "boehr.us.app-prod"));
    }
}
