package com.senko.scash.file.create;

import static com.senko.scash.Constants.PATH_SEPARATOR;
import static com.senko.scash.util.CmnUtil.removeDoubleQuate;
import static com.senko.scash.util.HSSFUtil.getStringCellValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.senko.scash.Constants;
import com.senko.scash.SubProperties;
import com.senko.scash.file.create.FileCreater.NullWriter;

public class ResultDataCreater extends FileCreater<NullWriter>{

    private ResultDataCreater(){}
    public static final ResultDataCreater RESULT_DATA = new ResultDataCreater();

    @Override
    public NullWriter init(SubProperties subProps, String name) throws IOException {
        System.out.println("RESULT:" + name);
        File file = new File(subProps.getResultPath() + name);
        if (!file.exists())
            file.mkdirs();
        return NullWriter.NULL;
    }

    @Override
    public void create(SubProperties subProps, String name, HSSFSheet sheet, NullWriter resource) throws IOException {
        List<String> columns = getColumns(sheet);
        if (columns.size() > 0){
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    subProps.getResultPath() + name + PATH_SEPARATOR + sheet.getSheetName() + ".csv"),
                            "UTF-8"));
            try {
                writer.append(createHeaderRow(columns));
                writer.newLine();

                HSSFRow row;
                int rowNum = DATA_START_ROW;
                while ((row = sheet.getRow(rowNum++)) != null && row.getCell(0) != null) {
                    String selectValues = createValueRow(row, columns.size());
                    if (selectValues.replaceAll(",", "").length() > 0) {
                        writer.append(selectValues);
                        writer.newLine();
                    }
                }
            }finally{
                writer.close();
            }
        }
    }

    @Override
    public void finish(SubProperties subProps, String name, NullWriter resource) throws IOException {

    }

    @Override
    protected String getOutPath(SubProperties subProps, String name) {
        return subProps.getOutResultPath() + name + Constants.PATH_SEPARATOR;
    }

    private static String createHeaderRow(List<String> columns) {
        StringBuilder sb = new StringBuilder();

        for (String column :columns)
            sb.append(column).append(",");
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    private static String createValueRow(HSSFRow row, int columnsSize) {
        StringBuilder csvRow = new StringBuilder();

        String cellVal = null;
        for (int i = 0; i < columnsSize; i++) {
            cellVal = getStringCellValue(row.getCell(i));
            if("\"\"".equals(cellVal)){
            	cellVal="";
            }
//            if (cellVal == null || "".equals(cellVal)) {
//                break;
//            }

            csvRow.append(removeDoubleQuate(cellVal));
            csvRow.append(",");
        }
        if (csvRow.length() > 0) {
            csvRow.deleteCharAt(csvRow.length() - 1);
        }
        return csvRow.toString();
    }


}
