package com.senko.scash.pattern.regex;

import static com.senko.scash.pattern.AnalyzeUtil.DATA_CREATORS;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.senko.scash.pattern.AnalyzeUtil.DATA_CREATOR;
import com.senko.scash.pattern.AnalyzeUtil.RESULT;
import com.senko.scash.pattern.regex.DataPattern.VALIDATE_RESULT;
import com.senko.scash.util.HSSFUtil;

public class Analyzer { // extends com.senko.scash.Analyzer<SubProperties>{

    public void analyze(String path) throws Exception {
        this.analyze(path, "入力値パターン");
    }

    private void analyze(String path, String patternSheetName) throws Exception {
        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(path));

        checkRegex(book.getSheet(patternSheetName));
    }

    private void checkRegex(HSSFSheet patternSheet) {

        for (DataPattern dataPattern : parsePattern(patternSheet)) {
            System.out.println("> NAME : " + dataPattern.getName() + " MIN : " + dataPattern.getMin() + " MAX : "
                    + dataPattern.getMax() + " PATTERN : " + dataPattern.getRegexPattern());
            for (DATA_CREATOR creator : DATA_CREATORS) {
                System.out.println("  > CREATOR : " + creator.name());
                if (creator.getResult() == RESULT.OK)
                    for (String regexValue : creator.create(dataPattern)) {
                        VALIDATE_RESULT result = dataPattern.validate(regexValue);
                        if (result == VALIDATE_RESULT.OK)
                            System.out.println("    >OK : [VALUE : " + regexValue + "]");
                        else
                            System.out.println("    *NG : [VALUE : " + regexValue + "] [RESULT : " + result.name()
                                    + "]");
                    }
                else
                    for (String regexValue : creator.create(dataPattern)) {
                        VALIDATE_RESULT result = dataPattern.validate(regexValue);
                        if (result != VALIDATE_RESULT.OK)
                            System.out.println("    >OK : [VALUE : " + regexValue + "] [RESULT : " + result.name()
                                    + "]");
                        else
                            System.out.println("    *NG : [VALUE : " + regexValue + "]");
                    }
            }
        }

    }

    private List<DataPattern> parsePattern(HSSFSheet patternSheet) {
        List<DataPattern> patterns = new ArrayList<DataPattern>();
        int rowId = 2;
        while (true) {
            HSSFRow row;
            try {
                row = patternSheet.getRow(rowId);
                if (row == null)
                    break;
                if (HSSFUtil.getStringCellValue(row.getCell(0)).equals(""))
                    break;
            } catch (NullPointerException e) {
                break;
            }
            rowId++;
            patterns.add(new DataPattern(row));
        }
        return patterns;
    }
}
