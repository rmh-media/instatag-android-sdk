package com.bi_instatag.itandroidsdk;

public class ReportSuiteSetting {
    protected String name;
    protected String devURL;
    protected String prodURL;

    public ReportSuiteSetting(String name, String devURL, String prodURL) {
        this.name = name;
        this.devURL = devURL;
        this.prodURL = prodURL;
    }

    public String getName() {
        return name;
    }

    public String getDevURL() {
        return devURL;
    }

    public String getProdURL() {
        return prodURL;
    }
}
