package com.senko.scash.pattern.cases;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.senko.scash.pattern.IValidPattern;
import com.senko.scash.util.HSSFUtil;

public class DataPattern extends com.senko.scash.pattern.DataPattern{
    private final String                 name;
    private final String                 errorResult;

    public DataPattern(HSSFRow line) {
        this(line,DEFAULTS);
    }

    public DataPattern(HSSFRow line,IValidPattern... patterns) {
        super(line,2,patterns);
        this.name        = HSSFUtil.getStringCellValue(line.getCell(0));
        this.errorResult = HSSFUtil.getStringCellValue(line.getCell(1));
    }


    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * errorResult getter
     * @return errorResult
     */
    public String getErrorResult() {
        return errorResult;
    }
}
