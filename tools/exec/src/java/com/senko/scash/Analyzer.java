package com.senko.scash;

import static com.senko.scash.Constants.HEADER_INPUT;
import static com.senko.scash.Constants.HEADER_OUTPUT;
import static com.senko.scash.util.HSSFUtil.getColumns;
import static com.senko.scash.util.HSSFUtil.getKeyValueMap;
import static com.senko.scash.util.HSSFUtil.getValue;
import static com.senko.scash.util.HSSFUtil.searchRow;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public abstract class Analyzer<S extends SubProperties>{

    /** データ列挙シートのヘッダ行(固定値) */
    protected static final int HEADER_ROW        = 0;
    /** データ列挙シートのカラム名称 */
    protected static final int COLUMN_HEADER_ROW = 2;
    /** データ列挙シートのデータ開始位置 */
    protected static final int BODY_ROW          = 3;

    public static class Columns{
        private int startLine = 0;
        private List<String> columns = null;
        public void setStartLine(int line){
            this.startLine = line;
        }
        public void setColumns(List<String> columns){
            this.columns = columns;
        }
        public int getStartLine(){
            return this.startLine;
        }
        public List<String> getColumns(){
            return this.columns;
        }
    }

    protected final MainProperties mainProps;
    protected final S subProps;

    protected Analyzer(MainProperties mainProps,S subProps){
        this.mainProps = mainProps;
        this.subProps  = subProps;
    }

    /**
     *
     * @param sheet
     * @return pattern list
     * @throws Exception
     */
    protected List<String[]> createFiles(HSSFSheet sheet) {
        Columns req = new Columns();
        Columns res = new Columns();

        setColumns(sheet,req,res);
        int rowId = BODY_ROW;

        List<String[]> list = new ArrayList<String[]>();
        while(true){
            HSSFRow row;
            try{
                row= sheet.getRow(rowId);
                if (row == null)
                    break;
            }catch(NullPointerException e){
                break;
            }
            String pattern = getValue(row,req.getStartLine() - 2);
            if (pattern == null || pattern.equals(""))
                break;
            convRowToFile(subProps.getRequestPath()
                    ,getRequestFileName(sheet.getSheetName(),pattern)
                    ,getKeyValueMap(row, req.getStartLine() , req.getColumns()));
            convRowToFile(subProps.getResponcePath()
                    ,getResponseFileName(sheet.getSheetName(),pattern)
                    ,getKeyValueMap(row, res.getStartLine() , res.getColumns()));
            rowId++;
            list.add(new String[]{pattern,getValue(row,req.getStartLine()-1)});
        }
        return list;
    }

    /**
     * Request&Response電文ファイル(Property形式)作成準備
     * @param sheet
     * @param reqColumns
     * @param resColumns
     */
    protected static void setColumns(HSSFSheet sheet,final Columns reqColumns,final Columns resColumns) {
        HSSFRow row;
        try{
            row = sheet.getRow(HEADER_ROW);
            if (row == null)
                return;
        }catch(NullPointerException e){
            return;
        }

        int inStartCellNo = searchRow(row, 0, HEADER_INPUT);

        // OUTPUT開始位置の設定
        int outStartCellNo = searchRow(row, inStartCellNo, HEADER_OUTPUT);

        row = sheet.getRow(COLUMN_HEADER_ROW);
        reqColumns.setStartLine(inStartCellNo);
        if (inStartCellNo != outStartCellNo)
            reqColumns.setColumns(getColumns(row, inStartCellNo, outStartCellNo));
        else
            reqColumns.setColumns(getColumns(row, inStartCellNo));

        resColumns.setStartLine(outStartCellNo);
        resColumns.setColumns(getColumns(row, outStartCellNo));
    }

    protected static void convRowToFile(String inPath,
            String fileName,
            Map<String, String> map){
        Writer writer = null;
        try{
            System.out.println("    " + inPath + fileName);
            writer = new BufferedWriter(new FileWriter(inPath + fileName));
            writer.append(createProperties(map));
            writer.flush();
        }catch(IOException e){
            throw new RuntimeException(e);
        }finally{
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected static String createProperties(Map<String,String> map){
        StringBuilder sb = new StringBuilder();
        for (Entry<String,String> entry : map.entrySet())
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\r\n");
        return sb.toString();
    }

    protected String getTestCase(String sheetName,String... patternNames){
        StringBuilder sb = new StringBuilder();
        sb.append(sheetName);
        for (String patternName:patternNames)
            sb.append("_").append(patternName);

        return sb.toString();
    };

    protected String getRequestFileName(String sheetName,String... patternNames){
        return "In_" + getTestCase(sheetName,patternNames) + ".dat";
    };

    protected String getResponseFileName(String sheetName,String... patternNames){
        return "Out_" + getTestCase(sheetName,patternNames) + ".dat";
    };

    public void createTestProperties() {
        FileWriter fw;
        try {
            fw = new FileWriter(subProps.getXmlPath() + "/test.properties");
            fw.write("test.mode=LocalTest\r\n");
            fw.write("target.host=localhost\r\n");
            fw.write("target.port=8080\r\n");
            fw.write("data.request=" + subProps.getOutRequestPath() + "\r\n");
            fw.write("data.response=" + subProps.getOutResponcePath() + "\r\n");
            fw.write("timeout.port=9999\r\n");
            fw.write("timeout.sleep.interval=5000\r\n");
            fw.write("timeout.sleep.weight=100\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected String getXmlHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >\n");
        sb.append("<suite name=\"auto_create\">\n");
        sb.append("<parameter name=\"testProperties\" value=\"" + subProps.getOutXmlPath() + "test.properties\"/>\n");
        return sb.toString();
    }

    protected String getXmlBody(TestXmlParameter test) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<test verbose=\"2\" name=\"").append(test.getCase()).append("\" annotations=\"JDK\">\n");
        sb.append("\t\t<parameter name=\"param\" value=\"").append(test.getValue() == null ? "" : test.getValue()).append("\"/>\n");
        if (test.getCase().endsWith(Constants.RELATION_HEADER_BEFORE)) {
            sb.append("\t\t<parameter name=\"simsql\" value=\"").append(test.getValueSim() == null ? "" : test.getValueSim()).append("\"/>\n");
        }
        sb.append("\t\t<classes>\n");
        sb.append("\t\t\t<class name=\"").append(test.getClazz()).append("\">\n");
        sb.append("\t\t\t\t<methods><include name=\"" + test.getMethod() + "\"/></methods>\n");
        sb.append("\t\t\t</class>\n");
        sb.append("\t\t</classes>\n");
        sb.append("\t</test>\n");

        return sb.toString();
    }

    protected String getXmlFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</suite>\n");
        return sb.toString();
    }

}
