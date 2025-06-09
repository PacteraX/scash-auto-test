package com.senko.scash.pattern;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.senko.scash.pattern.IValidInvalid;
import com.senko.scash.pattern.IValidPattern;
import com.senko.scash.pattern.IValidPattern.DEFAULT_VALID_PATTERN;
import com.senko.scash.util.HSSFUtil;

public abstract class DataPattern {
    protected enum SOURCE{
        MIN(0),
        MAX(1),
        PATTERN(2),
        VALID(3),
        INVALID(4);
        private final int col;
        private SOURCE(int col){
            this.col = col;
        }
    }

    protected static final SOURCE[]        values      = SOURCE.values();
    protected  static final IValidPattern[] DEFAULTS    = DEFAULT_VALID_PATTERN.values();

    private final List<SubPattern>       subPatterns = new ArrayList<SubPattern>();
    private final int                    sumMin;
    private final int                    sumMax;

    public DataPattern(HSSFRow line,int subPatternStart) {
        this(line,subPatternStart,DEFAULTS);
    }

    public DataPattern(HSSFRow line,int subPatternStart, IValidPattern... patterns) {

        while(true){
            if (HSSFUtil.getStringCellValue(line.getCell(subPatternStart)).equals(""))
                break;
            subPatterns.add(new SubPattern(patterns,line,subPatternStart));
            subPatternStart+=values.length;
        }
        int sumMin = 0;
        int sumMax = 0;
        for (SubPattern subPattern : subPatterns){
            sumMin += subPattern.min;
            sumMax += subPattern.max;
        }
        this.sumMin = sumMin;
        this.sumMax = sumMax;
    }

    public static class SubPattern{
        private final int min;
        private final int max;
        private final List<String> minValid   = new ArrayList<String>();
        private final List<String> maxValid   = new ArrayList<String>();
        private final List<String> minInvalid = new ArrayList<String>();
        private final List<String> maxInvalid = new ArrayList<String>();

        public SubPattern(IValidPattern[] validPatterns, String[] line,int colStart){
            this(validPatterns,Integer.parseInt(
                     line[colStart+SOURCE.MIN.col])
                    ,Integer.parseInt(line[colStart+SOURCE.MAX.col])
                    ,line[colStart+SOURCE.PATTERN.col]
                    ,line[colStart+SOURCE.VALID.col]
                    ,line[colStart+SOURCE.INVALID.col]);
        }

        public SubPattern(IValidPattern[] validPatterns, HSSFRow line,int colStart){
            this(validPatterns,Integer.parseInt(HSSFUtil.getStringCellValue(line.getCell(colStart+SOURCE.MIN.col)))
                    ,Integer.parseInt(HSSFUtil.getStringCellValue(line.getCell(colStart+SOURCE.MAX.col)))
                    ,HSSFUtil.getStringCellValue(line.getCell(colStart+SOURCE.PATTERN.col))
                    ,HSSFUtil.getStringCellValue(line.getCell(colStart+SOURCE.VALID.col))
                    ,HSSFUtil.getStringCellValue(line.getCell(colStart+SOURCE.INVALID.col)));
        }

        public SubPattern(IValidPattern[] validPatterns,int min,int max,String patternName,String validChars,String invalidChars){
            this.min = min;
            this.max = max;
            for (IValidPattern validPattern : validPatterns)
                if (validPattern.matchPatternName(patternName)){
                    IValidInvalid validInvalid = validPattern.getValidInvalid();
                    setChars(validInvalid.valid(),
                            validChars,
                            invalidChars,
                            minValid,
                            maxValid);
                    setChars(validInvalid.invalid(),
                             invalidChars,
                             validChars,
                             minInvalid,
                             maxInvalid);
                    return;
                }
            throw new RuntimeException("対象外のパターンです。 [" + patternName + "]");
        }

        private void setChars(char[][] charsArray,String include,String exclude,
                final List<String> mins,
                final List<String> maxs){
            for (char[] chars : charsArray){
                CHARS : for(char value : chars){
                    for(int i = 0; i < include.length(); i++)
                        if(value == include.charAt(i)){
                            include = include.replaceAll(String.valueOf(value), "");
                            break;
                        }
                    for(int i = 0; i < exclude.length(); i++)
                        if(value == exclude.charAt(i)){
                            continue CHARS;
                        }
                    mins.add(getPaddingString(value,this.min));
                    maxs.add(getPaddingString(value,this.max));
                }
            }
            for(int i = 0; i < include.length(); i++){
                mins.add(getPaddingString(include.charAt(i),this.min));
                maxs.add(getPaddingString(include.charAt(i),this.max));
            }

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

        /**
         * minValid getter
         * @return minValid
         */
        public List<String> getMinValid() {
            return minValid;
        }

        /**
         * maxValid getter
         * @return maxValid
         */
        public List<String> getMaxValid() {
            return maxValid;
        }

        /**
         * minInvalid getter
         * @return minInvalid
         */
        public List<String> getMinInvalid() {
            return minInvalid;
        }

        /**
         * maxInvalid getter
         * @return maxInvalid
         */
        public List<String> getMaxInvalid() {
            return maxInvalid;
        }

    }

    public static String getPaddingString(char target , int length){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(target);
        return sb.toString();
    }

    /**
     * subPatterns getter
     * @return subPatterns
     */
    public List<SubPattern> getSubPatterns() {
        return subPatterns;
    }

    /**
     * min getter
     * @return min
     */
    public int getSumMin() {
        return sumMin;
    }

    /**
     * max getter
     * @return max
     */
    public int getSumMax() {
        return sumMax;
    }
}
