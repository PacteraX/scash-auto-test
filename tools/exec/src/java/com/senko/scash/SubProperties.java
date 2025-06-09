package com.senko.scash;

import static com.senko.scash.Constants.PATH_IN_CASE;
import static com.senko.scash.Constants.PATH_OUT_PREDATA;
import static com.senko.scash.Constants.PATH_OUT_REQUEST;
import static com.senko.scash.Constants.PATH_OUT_RESPONSE;
import static com.senko.scash.Constants.PATH_OUT_RESPONSE_FILE;
import static com.senko.scash.Constants.PATH_OUT_RESULT;
import static com.senko.scash.Constants.PATH_OUT_XML;
import static com.senko.scash.Constants.PATH_TEST_DATA;

import java.util.Properties;

import com.senko.scash.util.CmnUtil;

public class SubProperties {
    private final Properties     props = new Properties();

    private final String name;
    private final String testClass;

    private final String   casePath;
    private final String   sqlFilePath;
    private final String   requestPath;
    private final String   responcePath;
    private final String   responceFilePath;
    private final String   xmlPath;
    private final String   resultPath;

    private final String   outSqlFilePath;
    private final String   outRequestPath;
    private final String   outResponcePath;
    private final String   outResponceFilePath;
    private final String   outXmlPath;
    private final String   outResultPath;

    public SubProperties(MainProperties main,String subPath,String name) {

        CmnUtil.loadProperties("conf/" + subPath + name + ".properties", props);

        this.name      = name;
        this.testClass = getProps().getProperty("test.class.name");

        this.casePath             = getPath(main.getInPath(), PATH_IN_CASE);
        this.sqlFilePath          = getPath(main.getInPath(), PATH_OUT_PREDATA);
        this.requestPath          = getPath(main.getInPath(), PATH_OUT_REQUEST);
        this.responcePath         = getPath(main.getInPath(), PATH_OUT_RESPONSE);
        this.responceFilePath     = getPath(main.getInPath(), PATH_OUT_RESPONSE_FILE);
        this.xmlPath              = getPath(main.getInPath(), PATH_OUT_XML);
        this.resultPath           = getPath(main.getInPath(), PATH_OUT_RESULT);

        this.outSqlFilePath       = getPath(main.getOutPath(), PATH_OUT_PREDATA);
        this.outRequestPath       = getPath(main.getOutPath(), PATH_OUT_REQUEST);
        this.outResponcePath      = getPath(main.getOutPath(), PATH_OUT_RESPONSE);
        this.outResponceFilePath  = getPath(main.getOutPath(), PATH_OUT_RESPONSE_FILE);
        this.outXmlPath           = getPath(main.getOutPath(), PATH_OUT_XML);
        this.outResultPath        = getPath(main.getOutPath(), PATH_OUT_RESULT);

        mkdirs();
    }

    private String getPath(String mainPath, String subPath){
        return mainPath + PATH_TEST_DATA + this.name + Constants.PATH_SEPARATOR + subPath;
    }
    private void mkdirs() {
        CmnUtil.mkdir(getSqlFilePath());
        CmnUtil.mkdir(getRequestPath());
        CmnUtil.mkdir(getResponcePath());
        CmnUtil.mkdir(getResponceFilePath());
        CmnUtil.mkdir(getXmlPath());
        CmnUtil.mkdir(getResultPath());
    }

    /**
     * props getter
     * @return props
     */
    public Properties getProps() {
        return props;
    }

    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * testClass getter
     * @return testClass
     */
    public String getTestClass() {
        return testClass;
    }

    /**
     * casePath getter
     * @return casePath
     */
    public String getCasePath() {
        return casePath;
    }

    /**
     * sqlFilePath getter
     * @return sqlFilePath
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }

    /**
     * requestPath getter
     * @return requestPath
     */
    public String getRequestPath() {
        return requestPath;
    }

    /**
     * responcePath getter
     * @return responcePath
     */
    public String getResponcePath() {
        return responcePath;
    }

    /**
     * responceFilePath getter
     * @return responceFilePath
     */
    public String getResponceFilePath() {
        return responceFilePath;
    }

    /**
     * xmlPath getter
     * @return xmlPath
     */
    public String getXmlPath() {
        return xmlPath;
    }

    /**
     * resultPath getter
     * @return resultPath
     */
    public String getResultPath() {
        return resultPath;
    }

    public String getExcelPath(int num) {
        return props.getProperty("excel.file." + num);
    }

    public String getRelationSheet(int num) {
        return props.getProperty("excel.relation." + num);
    }

    /**
     * outSqlFilePath getter
     * @return outSqlFilePath
     */
    public String getOutSqlFilePath() {
        return outSqlFilePath;
    }

    /**
     * outRequestPath getter
     * @return outRequestPath
     */
    public String getOutRequestPath() {
        return outRequestPath;
    }

    /**
     * outResponcePath getter
     * @return outResponcePath
     */
    public String getOutResponcePath() {
        return outResponcePath;
    }

    /**
     * outResponceFilePath getter
     * @return outResponceFilePath
     */
    public String getOutResponceFilePath() {
        return outResponceFilePath;
    }

    /**
     * outXmlPath getter
     * @return outXmlPath
     */
    public String getOutXmlPath() {
        return outXmlPath;
    }

    /**
     * outResultPath getter
     * @return outResultPath
     */
    public String getOutResultPath() {
        return outResultPath;
    }

}
