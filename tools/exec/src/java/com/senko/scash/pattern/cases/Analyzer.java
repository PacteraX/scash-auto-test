package com.senko.scash.pattern.cases;

import static com.senko.scash.pattern.AnalyzeUtil.DATA_CREATORS;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.senko.scash.MainProperties;
import com.senko.scash.TestXmlParameter;
import com.senko.scash.pattern.AnalyzeUtil.DATA_CREATOR;
import com.senko.scash.pattern.AnalyzeUtil.RESULT;

public class Analyzer extends com.senko.scash.Analyzer<SubProperties>{

    public Analyzer(MainProperties mainProps, SubProperties subProps) {
        super(mainProps, subProps);
    }

    public void analyze() throws Exception {
        int excelBookNum = 1;

        String excelBookName = null;
        while (true) {
            excelBookName = subProps.getExcelPath(excelBookNum);
            if (excelBookName == null) {
                break;
            }

            this.analyze(excelBookName, "正常値","入力値パターン");
            excelBookNum++;
        }
    }

    private void analyze(String excelBookName, String caseSheetName,String patternSheetName) throws Exception {
        System.out.println("---Analyze start.[" + excelBookName + "]---");

        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(subProps.getCasePath() + excelBookName));

        List<TestXmlParameter> caseList = this.createFiles(
                book.getSheet(caseSheetName),
                book.getSheet(patternSheetName));

        this.createXmlFile(caseList);
    }

   private List<TestXmlParameter> createFiles(HSSFSheet caseSheet,HSSFSheet patternSheet) {
       Columns reqColumns = new Columns();
       Columns resColumns = new Columns();

       setColumns(caseSheet,reqColumns,resColumns);

       List<DataPattern> patternList = parsePattern(patternSheet);

       int rowId = BODY_ROW;

       List<TestXmlParameter> caseList = new ArrayList<TestXmlParameter>();
       while(true){
           HSSFRow row;
           try{
               row= caseSheet.getRow(rowId);
               if (row == null)
                   break;
           }catch(NullPointerException e){
               break;
           }
           AnalyzeIO io = new AnalyzeIO(row,reqColumns,resColumns,patternList);
           if (io.getCasePattern() == null || io.getCasePattern().equals(""))
               break;
           convRowToFile(io);
           rowId++;
           caseList.addAll(io.getResultTestList());
       }
       return caseList;
   }

   private List<DataPattern> parsePattern(HSSFSheet patternSheet){
       List<DataPattern> patterns = new ArrayList<DataPattern>();
       int rowId = 2;
       while(true){
           HSSFRow row;
           try{
               row= patternSheet.getRow(rowId);
               if (row == null)
                   break;
           }catch(NullPointerException e){
               break;
           }
           rowId++;
           patterns.add(new DataPattern(row));
       }
       return patterns;
   }

   private void convRowToFile(AnalyzeIO io){

       for (DataPattern pattern : io.getPatternList())
           if (io.getCaseInputMap().containsKey(pattern.getName())){
               String inputTemporary  = io.getCaseInputMap().get(pattern.getName());
               String outputTemporary = io.getCaseOutputMap().get(pattern.getName());
               convRowToFile(io,pattern);
               io.getCaseInputMap().put(pattern.getName(),inputTemporary);
               io.getCaseOutputMap().put(pattern.getName(),outputTemporary);
           }
   }

   private void convRowToFile(AnalyzeIO io,DataPattern targetPattern){

       String resultTemporary = io.getCaseOutputMap().get("result");

       for (DATA_CREATOR creator : DATA_CREATORS){
           if (creator.getResult() == RESULT.OK)
               io.getCaseOutputMap().put("result", resultTemporary);
           else
               io.getCaseOutputMap().put("result", targetPattern.getErrorResult());

           List<String> patterns = creator.create(targetPattern);
           for (int i = 0 ; i < patterns.size() ; i++){
               io.getCaseInputMap().put(targetPattern.getName(),patterns.get(i));
               String fileName = getRequestFileName(creator.name(),targetPattern.getName(),Integer.toString(i));
               convRowToFile(subProps.getRequestPath()
                       ,fileName
                       ,io.getCaseInputMap());
               fileName = getResponseFileName(creator.name(),targetPattern.getName(),Integer.toString(i));
               convRowToFile(subProps.getResponcePath()
                       ,fileName
                       ,io.getCaseOutputMap());
               String caze = getTestCase(creator.name(),targetPattern.getName(),Integer.toString(i));
               io.addResultTestList(creator.name(),caze,caze,subProps.getTestClass(), io.getCaseMethod());
           }
       }

       io.getCaseOutputMap().put("result", resultTemporary);

   }

   private void createXmlFile(List<TestXmlParameter> paramList) throws IOException {
       if (paramList.size() < 0)
           System.out.println("パターンがありません。");

       FileWriter fwAll = new FileWriter(subProps.getXmlPath() + "/testng_auto_create.xml");
       fwAll.write(getXmlHeader());
       for (TestXmlParameter param : paramList){
           String body = getXmlBody(param);
           fwAll.append(body);
       }
       fwAll.append(getXmlFooter());
       fwAll.close();
       System.out.println("testng.xml作成==============================");
   }
}
