package com.senko.scash.file.create;

import static com.senko.scash.util.HSSFUtil.getCellValueWithQuort;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.senko.scash.SubProperties;

public class BeforeSQLCreater extends FileCreater<BufferedWriter> {

    private static final String IGNORE_SIGN = "${ignoreDelKey}";

    private enum NO_TRUNC_TBL {

        ronri_tbl_dat_gyo("ronri_tbl_id", "gyo_no"),
        ronri_tbl_koumk_teigi("ronri_tbl_id", "retsu_id"),
        ronri_tbl_teigi("ronri_tbl_id"),
        msg_resources("key_cd"),
        ;

        private String[] keys;

        private NO_TRUNC_TBL(String... keys) {
            this.keys = keys;
        }

        public static boolean isNoTruncTbl(String tblName) {
            for (NO_TRUNC_TBL tbl : NO_TRUNC_TBL.values()) {
                if (tblName.equalsIgnoreCase(tbl.name()))
                    return true;
            }

            return false;
        }

        public static NO_TRUNC_TBL getEnum(String tblName) {
            for (NO_TRUNC_TBL tbl : NO_TRUNC_TBL.values()) {
                if (tblName.equalsIgnoreCase(tbl.name()))
                    return tbl;
            }

            return null;
        }
    }

    private BeforeSQLCreater() {
    }

    public static final BeforeSQLCreater BEFORE_SQL = new BeforeSQLCreater();

    @Override
    protected BufferedWriter init(SubProperties subProps, String name) throws IOException{
        System.out.println("SQL:" + name);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(subProps.getSqlFilePath() + name + ".sql"),
                                                         "UTF-8"));
    }

    @Override
    protected void create(SubProperties subProps, String name, HSSFSheet sheet, BufferedWriter resource)
                                                                                                        throws IOException {

        if (NO_TRUNC_TBL.isNoTruncTbl(sheet.getSheetName())) {
            for (String delRow : createDeleteRows(sheet)) {
                resource.append(delRow);
                resource.newLine();
            }
        } else {
            resource.append("TRUNCATE TABLE " + sheet.getSheetName());
            resource.newLine();
        }

        List<String> columns = getColumns(sheet);
        if (columns.size() > 0){
            String insertKeys = createInsertKeys(columns);

            HSSFRow row;
            int rowNum = DATA_START_ROW;
            // 行がnullまたは、1項目目がnullだった場合処理を終了
            while ((row = sheet.getRow(rowNum++)) != null && row.getCell(0) != null){
                String insertValues = createInsertValues(row,columns.size());
                if (insertValues.replaceAll(",", "").length() > 0) {
                    resource.append(createInsert(sheet.getSheetName(), insertKeys, clearIgnoreSign(insertValues)));
                    resource.newLine();
                }
            }

        }
        resource.newLine();
    }

    @Override
    protected void finish(SubProperties subProps, String name, BufferedWriter resource) throws IOException{
        if (resource != null) {
            resource.flush();
            resource.close();
        }
    }

    @Override
    protected String getOutPath(SubProperties subProps, String name) {
        return subProps.getOutSqlFilePath() + name + ".sql";
    }


    private static String createInsertKeys(List<String> columns){
        StringBuilder sb = new StringBuilder();

        for (String column :columns)
            sb.append(column).append(",");
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    private static String createInsertValues(HSSFRow row , int columnsSize){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnsSize; i++){
            HSSFCell cell = row.getCell(i);
            if ("'TIMESTAMP'".equals(getCellValueWithQuort(cell))) {
                sb.append("TO_CHAR(SYSDATE,'yyyymmddhh24miss')");
            }
            else {
                sb.append(getCellValueWithQuort(cell));
            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    private static String createInsert(String sheetName , String insertkeys , String insertValues){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + sheetName + " (");
        sb.append(insertkeys).append(") VALUES(");
        sb.append(insertValues).append(")");
        return sb.toString();
    }

    private static List<String> createDeleteRows(HSSFSheet sheet) {
        List<String> deleteRows = new ArrayList<String>();
        if (!NO_TRUNC_TBL.isNoTruncTbl(sheet.getSheetName())) {
            return deleteRows;
        }

        List<String> columns = getColumns(sheet);
        if (columns.size() > 0) {
            HSSFRow row;
            int rowNum = DATA_START_ROW;
            String delRow;
            // 行がnullまたは、1項目目がnullだった場合処理を終了
            while ((row = sheet.getRow(rowNum++)) != null && row.getCell(0) != null) {
                String insertValues = createInsertValues(row, columns.size());
                if (insertValues.replaceAll(",", "").length() > 0) {
                    delRow = createDelete(sheet.getSheetName(), row, columns);
                    if (!deleteRows.contains(delRow)) {
                        deleteRows.add(delRow);
                    }
                }
            }

        }
        return deleteRows;
    }

    private static String createDelete(String sheetName, HSSFRow row, List<String> columns) {
        NO_TRUNC_TBL tblNoTrunc = NO_TRUNC_TBL.getEnum(sheetName);
        if (tblNoTrunc == null)
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(sheetName);

        int idx = 0;
        String value;
        for (String keyEnum : tblNoTrunc.keys) {
            value = getValueByKey(keyEnum, row, columns);
            if (!value.toLowerCase().contains(IGNORE_SIGN.toLowerCase())) {
                if (idx == 0) {
                    sb.append(" WHERE ");
                } else {
                    sb.append(" AND ");
                }

                sb.append(keyEnum).append("=").append(value);
                idx++;
            }
        }
        return sb.toString();
    }

    private static String getValueByKey(String key, HSSFRow row, List<String> columns) {
        int colidx = 0;
        boolean hasCol = false;
        String cell = "";
        for (String colname : columns) {
            if(key.equalsIgnoreCase(colname)){
                hasCol = true;
                break;
            }
            colidx++;
        }

        if (hasCol) {
            cell = getCellValueWithQuort(row.getCell(colidx));
        }

        return cell;
    }

    private static String clearIgnoreSign(String val) {
        int idx;
        while ((idx = val.toLowerCase().indexOf(IGNORE_SIGN.toLowerCase())) > 0) {
            val = new StringBuilder().append(val).delete(idx, idx + IGNORE_SIGN.length()).toString();
        }

        return val;
    }

}
