package com.senko.scash.util;

import static com.senko.scash.util.CmnUtil.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class HSSFUtil {

    private static final int SEARCH_LIMIT  = 200;

    public static int searchRow(HSSFRow row, int startCell, String target) {
        int cellNo = startCell;
        while (true) {
            HSSFCell cell = row.getCell(cellNo);
            if (!isBlankCell(cell) && getStringCellValue(row.getCell(cellNo)).indexOf(target) >= 0)
                break;
            if (cellNo > SEARCH_LIMIT)
                throw new RuntimeException("SEARCH LIMIT OVER.");
            cellNo++;
        }
        return cellNo;
    }

    public static List<String> getColumns(HSSFRow row, int start) {
        List<String> list = new ArrayList<String>();
        for (int i = start; !isBlankCell(row.getCell(i)); i++)
            list.add(getStringCellValue(row.getCell(i)));
        return list;
    }

    public static List<String> getColumns(HSSFRow row, int start, int end) {
        List<String>list = new ArrayList<String>();
        for (int i = start; i < end; i++)
            list.add(getStringCellValue(row.getCell(i)));
        return list;
    }


    public static boolean isBlankCell(HSSFCell cell) {
        boolean result = (cell == null);
        if (!result)
            result = "".equals(getCellValueWithQuort(cell));
        return result;
    }

    public static String getCellValueWithQuort(HSSFCell cell) {
        int type = cell.getCellType();
        if (type == HSSFCell.CELL_TYPE_BLANK)
            return "";
        else if (type == HSSFCell.CELL_TYPE_BOOLEAN)
            return "" + cell.getBooleanCellValue();
        else if (type == HSSFCell.CELL_TYPE_NUMERIC)
            return "" + cell.getNumericCellValue();
        else if (type == HSSFCell.CELL_TYPE_STRING){
            String value = removeDoubleQuate(cell.getStringCellValue().trim()).replace("'", "''");
            if(value.startsWith("TO_CHAR")){
                return value;
            }
            else if (value.length() == 0){
                return "'`'"; // Oracle対応後のID決済システムではバッククォートを空として扱う
            }
            else {
                return "'" + value + "'";
            }
        }
        else
            throw new RuntimeException("No Value from Cell");
    }

    public static String getValue(HSSFRow row, Integer idx){
        try{
            return getStringCellValue(row.getCell(idx));
        }catch(NullPointerException e){
            return "";
        }
    }

    public static String getStringCellValue(HSSFCell cell) {
        if (cell == null)
            return "";
        int type = cell.getCellType();
        if (type == HSSFCell.CELL_TYPE_BLANK)
            return "";
        else if (type == HSSFCell.CELL_TYPE_BOOLEAN)
            return "" + cell.getBooleanCellValue();
        else if (type == HSSFCell.CELL_TYPE_NUMERIC)
            return "" + (long) cell.getNumericCellValue();
        else if (type == HSSFCell.CELL_TYPE_STRING)
            return cell.getStringCellValue();
        else if (type == HSSFCell.CELL_TYPE_FORMULA)
            throw new RuntimeException("計算式はデータとして取得できないエクセプション [" + cell.getCellFormula() + "]");
        else
            throw new RuntimeException("No Value from Cell");
    }

    public static Map<String,String> getKeyValueMap(HSSFRow row, int startCol , List<String>columns){
        Map<String,String> map = new HashMap<String, String>();
        for (int i = startCol; i < columns.size() + startCol; i++) {
            map.put(columns.get(i - startCol), unicodeEscape(trimChar(getStringCellValue(row.getCell(i)),"\"")));
        }
        return map;
    }

    public static void getListKeyValueMap(HSSFRow row, int startCol , List<String>columns,Map<String,String> map){
        for (int i = startCol; i < columns.size() + startCol; i++) {
            int col = i - startCol;
            String data = map.get(columns.get(col));
            if (data == null)
                map.put(columns.get(col), unicodeEscape(trimChar(getStringCellValue(row.getCell(i)),"\"")));
            else
                map.put(columns.get(col), data + "," + unicodeEscape(trimChar(getStringCellValue(row.getCell(i)),"\"")));
        }
    }

}
