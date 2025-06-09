package com.senko.scash.pattern.cases;

import static com.senko.scash.util.HSSFUtil.getKeyValueMap;
import static com.senko.scash.util.HSSFUtil.getValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.senko.scash.TestXmlParameter;
import com.senko.scash.Analyzer.Columns;

public class AnalyzeIO {
    private final String              casePattern;
    private final String              caseMethod;
    private final Map<String, String> caseInputMap;
    private final Map<String, String> caseOutputMap;
    private final List<DataPattern>   patternList;
    private final List<TestXmlParameter> resultTestList;

    public AnalyzeIO(HSSFRow row, Columns reqColumns, Columns resColumns, List<DataPattern> patternList) {
        this.casePattern = getValue(row, reqColumns.getStartLine() - 2);
        this.caseMethod = getValue(row, reqColumns.getStartLine() - 1);
        this.caseInputMap = getKeyValueMap(row, reqColumns.getStartLine(), reqColumns.getColumns());
        this.caseOutputMap = getKeyValueMap(row, resColumns.getStartLine(), resColumns.getColumns());
        this.patternList = patternList;
        this.resultTestList = new ArrayList<TestXmlParameter>();
    }

    /**
     * casePattern getter
     *
     * @return casePattern
     */
    public String getCasePattern() {
        return casePattern;
    }

    /**
     * caseMethod getter
     *
     * @return caseMethod
     */
    public String getCaseMethod() {
        return caseMethod;
    }

    /**
     * caseInputMap getter
     *
     * @return caseInputMap
     */
    public Map<String, String> getCaseInputMap() {
        return caseInputMap;
    }

    /**
     * caseOutputMap getter
     *
     * @return caseOutputMap
     */
    public Map<String, String> getCaseOutputMap() {
        return caseOutputMap;
    }

    /**
     * patternList getter
     *
     * @return patternList
     */
    public List<DataPattern> getPatternList() {
        return patternList;
    }

    /**
     * resultTestList getter
     *
     * @return resultTestList
     */
    public List<TestXmlParameter> getResultTestList() {
        return resultTestList;
    }

    public void addResultTestList(String name, String caze ,String value ,String clazz,String method) {
        resultTestList.add(new TestXmlParameter(name,caze,value,clazz,method));
    }

}
