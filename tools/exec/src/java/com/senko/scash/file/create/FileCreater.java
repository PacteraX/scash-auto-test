package com.senko.scash.file.create;

import static com.senko.scash.util.HSSFUtil.getStringCellValue;
import static com.senko.scash.util.HSSFUtil.isBlankCell;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.senko.scash.SubProperties;

public abstract class FileCreater<C extends Writer> {
    protected static final class NullWriter extends Writer {public static final NullWriter NULL = new NullWriter();
        @Override
        public void close() throws IOException {
            throw new RuntimeException("NullWriter can't close.");
        }

        @Override
        public void flush() throws IOException {
            throw new RuntimeException("NullWriter can't flush.");
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new RuntimeException("NullWriter can't write.");
        }
    }
    protected static final List<String> BLANK = Collections.unmodifiableList(new ArrayList<String>());
    protected static final int COLUMN_ROW     = 1;
    protected static final int DATA_START_ROW = 2;

    public String create(SubProperties subProps,String name)throws IOException {
        if (name == null || name.length() <= 0)
            return null;

        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(subProps.getCasePath() + name + ".xls"));
        C resource = init(subProps,name);
        try{
            for (int i = 0; i < book.getNumberOfSheets(); i++)
                create(subProps,name,book.getSheetAt(i),resource);
        }finally{
            finish(subProps,name,resource);
        }
        return getOutPath(subProps, name);
    }

    protected abstract C init(SubProperties subProps,String name) throws IOException;
    protected abstract void create(SubProperties subProps,String name,HSSFSheet sheet,C resource) throws IOException;
    protected abstract void finish(SubProperties subProps,String name,C resource) throws IOException;
    protected abstract String getOutPath(SubProperties subProps,String name);

    protected static List<String> getColumns(final HSSFSheet sheet){
        HSSFRow row = sheet.getRow(COLUMN_ROW);
        int col = 0;
        HSSFCell cell;
        if (row == null || isBlankCell(cell = row.getCell(col)))
            return BLANK;
        else{
            List<String> columns = new ArrayList<String>();
            do {
                columns.add(getStringCellValue(cell));
            } while(!isBlankCell(cell = row.getCell(++col)));
            return columns;
        }
    }
}
