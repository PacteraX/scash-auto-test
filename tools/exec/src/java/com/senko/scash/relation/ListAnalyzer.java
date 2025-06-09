package com.senko.scash.relation;

import static com.senko.scash.Constants.METHOD_NAME_COL;
import static com.senko.scash.util.HSSFUtil.getListKeyValueMap;
import static com.senko.scash.util.HSSFUtil.getValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.senko.scash.MainProperties;
import com.senko.scash.TestXmlParameter;

public class ListAnalyzer extends Analyzer{


    public ListAnalyzer(MainProperties mainProps, String systemName) {
        super(mainProps, new SubProperties(mainProps,systemName));
    }

    public ListAnalyzer(MainProperties mainProps, SubProperties subProps) {
        super(mainProps, subProps);
    }

    @Override
    protected void setRowDatas(List<String[]> rowData
            ,String sheetName,String clazz,String prefix,String suffix,String single
            ,final List<TestXmlParameter> output){
        if (rowData.isEmpty())
            return;
        String caze = getTestCase(sheetName);
        String method  = rowData.get(0)[METHOD_NAME_COL];
        TestXmlParameter param = new TestXmlParameter(
                 sheetName
                ,caze
                ,caze
                ,clazz
                ,method
                ,rowData.size())
        .setPrefix(prefix)
        .setSuffix(suffix)
        .setSingle(single);
        output.add(param);
    }

    /**
    *
    * @param sheet
    * @return pattern list
    * @throws Exception
    */
    @Override
   protected List<String[]> createFiles(HSSFSheet sheet) {
       Columns req = new Columns();
       Columns res = new Columns();

       setColumns(sheet,req,res);
       int rowId = BODY_ROW;

       List<String[]> list = new ArrayList<String[]>();
       Map<String,String> inMap = new HashMap<String, String>();
       Map<String,String> outMap = new HashMap<String, String>();
       while(true){
           HSSFRow row;
           try{
               row= sheet.getRow(rowId);
               if (row == null)
                   break;
           }catch(NullPointerException e){
               break;
           }
           getListKeyValueMap(row, req.getStartLine() , req.getColumns(),inMap);
           getListKeyValueMap(row, res.getStartLine() , res.getColumns(),outMap);
           if (list.size() == 0)
               list.add(new String[]{getValue(row,req.getStartLine() - 2),getValue(row,req.getStartLine()-1)});

           rowId++;
       }
       if (inMap.size() != 0){
           convRowToFile(subProps.getRequestPath()
                   ,getRequestFileName(sheet.getSheetName())
                   ,inMap);
           convRowToFile(subProps.getResponcePath()
                   ,getResponseFileName(sheet.getSheetName())
                   ,outMap);
       }
       return list;
   }
}
