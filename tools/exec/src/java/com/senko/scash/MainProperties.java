package com.senko.scash;

import java.util.Properties;

import com.senko.scash.util.CmnUtil;

public  class MainProperties {
    private final Properties props = new Properties();
    private final String     inPath;
    private final String     outPath;

    public MainProperties(String path) {
        CmnUtil.loadProperties(path, props);
        inPath = props.getProperty("base.path");
        outPath = props.getProperty("output.base.path");
    }

    public String getInPath() {
        return inPath;
    }

    public String getOutPath() {
        return outPath;
    }

}
