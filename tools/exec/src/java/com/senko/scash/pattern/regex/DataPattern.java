package com.senko.scash.pattern.regex;

import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.senko.scash.pattern.IValidPattern;
import com.senko.scash.util.HSSFUtil;

public class DataPattern extends com.senko.scash.pattern.DataPattern{

    public enum VALIDATE_RESULT{
         OK
        ,ERR_UNDER_MIN
        ,ERR_OVER_MAX
        ,ERR_PATTERN
    }

    private final String                 name;
    private final int                    min;
    private final int                    max;
    private final Pattern                regexPattern;

    public DataPattern(HSSFRow line) {
        this(line,DEFAULTS);
    }

    public DataPattern(HSSFRow line,IValidPattern... patterns) {
        super(line,4,patterns);
        this.name         = HSSFUtil.getStringCellValue(line.getCell(0));
        this.min          = Integer.parseInt(HSSFUtil.getStringCellValue(line.getCell(1)));
        this.max          = Integer.parseInt(HSSFUtil.getStringCellValue(line.getCell(2)));
        this.regexPattern = Pattern.compile(HSSFUtil.getStringCellValue(line.getCell(3)));
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
    public VALIDATE_RESULT validate(String target) {
        if (this.min != -1 && this.min > target.length())
            return VALIDATE_RESULT.ERR_UNDER_MIN;
        else if (this.max != -1 && this.max < target.length())
            return VALIDATE_RESULT.ERR_OVER_MAX;
        else if (!this.regexPattern.matcher(target).matches())
            return VALIDATE_RESULT.ERR_PATTERN;
        return VALIDATE_RESULT.OK;

    }
    /**
     * min getter
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     * max getter
     * @return max
     */
    public int getMax() {
        return max;
    }

    public String getRegexPattern(){
        return this.regexPattern.pattern();
    }
}
