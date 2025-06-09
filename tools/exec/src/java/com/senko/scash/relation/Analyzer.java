package com.senko.scash.relation;

import static com.senko.scash.Constants.METHOD_NAME_COL;
import static com.senko.scash.Constants.RELATION_HEADER_BEFORE;
import static com.senko.scash.Constants.RELATION_HEADER_CLASS;
import static com.senko.scash.Constants.RELATION_HEADER_INPUT_SHEET_NAME;
import static com.senko.scash.Constants.RELATION_HEADER_PREFIX;
import static com.senko.scash.Constants.RELATION_HEADER_RESULT;
import static com.senko.scash.Constants.RELATION_HEADER_SINGLE;
import static com.senko.scash.Constants.RELATION_HEADER_SUFFIX;
import static com.senko.scash.Constants.TEST_CASE_COL;
import static com.senko.scash.util.CmnUtil.isNull;
import static com.senko.scash.util.HSSFUtil.getStringCellValue;
import static com.senko.scash.util.HSSFUtil.getValue;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.senko.scash.Constants;
import com.senko.scash.MainProperties;
import com.senko.scash.TestXmlParameter;
import com.senko.scash.file.create.BeforeSQLCreater;
import com.senko.scash.file.create.ResultDataCreater;

public class Analyzer extends com.senko.scash.Analyzer<SubProperties> {

    private final Map<String, Integer> relMap = new HashMap<String, Integer>();

    public Analyzer(MainProperties mainProps, String systemName) {
        super(mainProps, new SubProperties(mainProps,systemName));
    }

    public Analyzer(MainProperties mainProps, SubProperties subProps) {
        super(mainProps, subProps);
    }

    public void analyze() throws Exception {
        int excelBookNum = 1;

        String excelBookName = null;
        String relationSheetName = null;
        while (true) {
            excelBookName = subProps.getExcelPath(excelBookNum);
            if (excelBookName == null) {
                break;
            }

            relationSheetName = subProps.getRelationSheet(excelBookNum);
            if (relationSheetName == null)
                System.err.println("[SKIPPED] No Relation Setting on properties. [" + excelBookName + "]");
            else
                this.analyze(excelBookName, relationSheetName);
            excelBookNum++;
        }
    }

    protected String getRelValue(HSSFRow row, String columnName) {
        return getValue(row,relMap.get(columnName));
    }

    private void analyze(String excelBookName, String relationSheetName) throws Exception {
        System.out.println("---Analyze start.[" + subProps.getCasePath() + excelBookName + "]---");

        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(subProps.getCasePath() + excelBookName));
        HSSFSheet relSheet = book.getSheet(relationSheetName);
        HSSFRow tmpRelHeader = relSheet.getRow(Constants.RELATION_HEADER_START_ROW);

        getRelationHeader(tmpRelHeader);

        // リレーション表のheader構成チェック。不正な場合エラー終了。
        if (!isValidRelationHeader()) {
            System.out.println("リレーション表の構成が正しくありません。");
            return;
        }

        int currentRowNo = Constants.RELATION_HEADER_START_ROW + 1;
        HSSFRow relationRow = null;
        String sheetName = null;

        List<TestXmlParameter> xmlParamsList = new ArrayList<TestXmlParameter>();

        while (true) {
            if ((relationRow = relSheet.getRow(currentRowNo)) == null
                    || isNull(sheetName = getRelValue(relationRow, RELATION_HEADER_INPUT_SHEET_NAME)))
                break;
            System.out.println("  SHEET : " + sheetName);
            List<String[]> patternList =createFiles(book.getSheet(sheetName));

            String before = BeforeSQLCreater.BEFORE_SQL.create(subProps
                            , getRelValue(relationRow,RELATION_HEADER_BEFORE));
            String before_sim = BeforeSQLCreater.BEFORE_SQL.create(subProps
                            , getRelValue(relationRow,Constants.RELATION_HEADER_BEFORE_SIM));
            String adjust = getRelValue(relationRow,Constants.RELATION_HEADER_ADJUST);

            String result = ResultDataCreater.RESULT_DATA.create(subProps
                            , getRelValue(relationRow,RELATION_HEADER_RESULT));
            xmlParamsList.addAll(setXmlParams(relationRow, patternList,before,before_sim,adjust,result));
            currentRowNo++;
        }

        this.createXmlFile(xmlParamsList);
    }

    private List<TestXmlParameter> setXmlParams(HSSFRow relationRow, List<String[]> rowDatas,String before,String before_sim,String adjust,String result) {

        String sheetName = getRelValue(relationRow, RELATION_HEADER_INPUT_SHEET_NAME);
        String clazz     = getRelValue(relationRow, RELATION_HEADER_CLASS);

        String prefix    = getRelValue(relationRow,RELATION_HEADER_PREFIX);
        String suffix    = getRelValue(relationRow,RELATION_HEADER_SUFFIX);
        String single    = getRelValue(relationRow, RELATION_HEADER_SINGLE);


        if (isNull(clazz))
            clazz = subProps.getTestClass();

        List<TestXmlParameter> output = new ArrayList<TestXmlParameter>();
        if (before != null || before_sim != null){
            TestXmlParameter param = new TestXmlParameter(
                    sheetName
                    ,getTestCase(sheetName , "before")
                    ,before
                    ,clazz
                    ,"beforeSimDate")
            .setPrefix(prefix)
            .setSuffix(suffix)
            .setValueSim(before_sim)
            .setSingle(single);
            output.add(param);
        }
        if(!adjust.equals("")){
            TestXmlParameter param = new TestXmlParameter(
                    sheetName
                    ,getTestCase(sheetName , "adjust")
                    ,adjust
                    ,clazz
                    ,"adjust")
            .setPrefix(prefix)
            .setSuffix(suffix)
            .setSingle(single);
            output.add(param);
        }
        setRowDatas(rowDatas,sheetName,clazz,prefix,suffix,single, output);

        if (result != null){
            TestXmlParameter param = new TestXmlParameter(
                     sheetName
                    ,getTestCase(sheetName , "result")
                    ,result
                    ,clazz
                    ,"result")
            .setPrefix(prefix)
            .setSuffix(suffix)
            .setSingle(single);

            output.add(param);
        }

        return output;
    }

    protected void setRowDatas(List<String[]> rowDatas
            ,String sheetName,String clazz,String prefix,String suffix,String single
            ,final List<TestXmlParameter> output){
        for (String[] row : rowDatas){
            String caze = getTestCase(sheetName, row[TEST_CASE_COL]);
            String method  = row[METHOD_NAME_COL];
            TestXmlParameter param = new TestXmlParameter(
                     sheetName
                    ,caze
                    ,caze
                    ,clazz
                    ,method)
            .setPrefix(prefix)
            .setSuffix(suffix)
            .setSingle(single);
            output.add(param);
        }
    }

    private void createXmlFile(List<TestXmlParameter> paramList) throws IOException {
        if (paramList.size() <= 0){
            System.out.println("ケースが存在しません。");
            return;
        }
        String backName = paramList.get(0).getFileName();
        FileWriter fwAll = new FileWriter(subProps.getXmlPath() + "/testng_auto_create.xml");
        FileWriter fw = new FileWriter(subProps.getXmlPath() + "/testng_" + backName + ".xml");
        fwAll.write(getXmlHeader());
        fw.write(getXmlHeader());

        // --------単独起動の際でも、Relationを読み込むようになる.start---------
        StringBuffer relationBody = new StringBuffer();
        // --------単独起動の際でも、Relationを読み込むようになる.end---------

        for (TestXmlParameter param : paramList){
            String name    = param.getFileName();
            if (!backName.equals(name)){
                fw.write(getXmlFooter());
                fw.close();
                backName = name;
                fw = new FileWriter(subProps.getXmlPath() + "/testng_" + name + ".xml");
                fw.write(getXmlHeader());
                // --------単独起動の際でも、Relationを読み込むようになる.start---------
                if (relationBody.length() > 0) {
                    fw.append(relationBody);
                }
                // --------単独起動の際でも、Relationを読み込むようになる.end---------
            }

            String body    = getXmlBody(param);
            // --------単独起動の際でも、Relationを読み込むようになる.start---------
            if (Constants.RELATION_SHEET_NAME.equals(name)) {
                relationBody.append(body);
            }
            // --------単独起動の際でも、Relationを読み込むようになる.end---------
            fw.append(body);
            if (!param.isSingle()) {
                fwAll.append(body);
            }
        }
        fwAll.append(getXmlFooter());
        fwAll.close();
        fw.write(getXmlFooter());
        fw.close();
        System.out.println("testng.xml作成==============================");
    }

    private void getRelationHeader(HSSFRow headerRow) {
        int currentCellNo = 0;
        while (currentCellNo < 20) {
            HSSFCell cell = headerRow.getCell(currentCellNo);
            if (cell != null) {
                String value = getStringCellValue(cell);
                if (value != null && !value.equals(""))
                    relMap.put(value.toLowerCase(), currentCellNo);
            }
            currentCellNo++;
        }
    }

    public boolean isValidRelationHeader(){
        return true;
    }
}
