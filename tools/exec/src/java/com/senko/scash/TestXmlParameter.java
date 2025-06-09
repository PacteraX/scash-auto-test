package com.senko.scash;

import com.senko.scash.util.CmnUtil;

public class TestXmlParameter {
    private final String name;
    private final String caze;
    private final String value;
    private final String clazz;
    private final String method;
    private String valueSim;
    private String prefix;
    private String suffix;
    private String single;

    public TestXmlParameter(String name, String caze ,String value ,String clazz,String method){
        this.name = name;
        this.caze = caze;
        this.value = value;
        this.clazz = clazz;
        this.method = method;
        this.valueSim = "";
        this.prefix = "";
        this.suffix = "";
        this.single = "";
    }
    public TestXmlParameter(String name, String caze ,String value ,String clazz,String method,int size){
        this.name = name;
        this.caze = caze;
        this.value = value;
        this.clazz = clazz;
        this.method = method;
        this.valueSim = "";
        this.prefix = "";
        this.suffix = "";
        this.single = "";
    }
    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * caze getter
     * @return caze
     */
    public String getCase() {
        return caze;
    }

    /**
     * value getter
     * @return value
     */
    public String getValue() {
        return value;
    }
    /**
     * clazz getter
     * @return clazz
     */
    public String getClazz() {
        return clazz;
    }
    /**
     * method getter
     * @return method
     */
    public String getMethod() {
        if (method == null || method.length() == 0)
            return "doTest";
        else
            return "do" + method.substring(0, 1).toUpperCase() + method.substring(1);
    }

    /**
     * prefix getter
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * prefix setter
     * @param prefix prefix
     */
    public TestXmlParameter setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * suffix getter
     * @return suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * suffix setter
     * @param suffix suffix
     */
    public TestXmlParameter setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * single getter
     * @return single
     */
    public boolean isSingle() {
        return !CmnUtil.isNull(single);
    }

    /**
     * single setter
     * @param single single
     */
    public TestXmlParameter setSingle(String single) {
        this.single = single;
        return this;
    }

    public String getFileName(){
        if(prefix != null && prefix.length() > 0)
            prefix += "-";
        else
            prefix = "";

        if(suffix != null && suffix.length() > 0)
            suffix = "-" + suffix;
        else
            suffix = "";
        return prefix + name + suffix;
    }

    public TestXmlParameter setValueSim(String valueSim) {
        this.valueSim = valueSim;
        return this;
    }

    public String getValueSim() {
        return valueSim;
    }
}
