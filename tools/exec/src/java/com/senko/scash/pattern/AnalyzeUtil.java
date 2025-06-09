package com.senko.scash.pattern;

import java.util.ArrayList;
import java.util.List;

import static com.senko.scash.pattern.DataPattern.SubPattern;

public class AnalyzeUtil {

    private static final String[] BLANK              = {};

    public enum RESULT{
        OK,ERROR
    }

    public enum DATA_CREATOR{
        VALID_MIN(RESULT.OK) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.validMin(ptn);
            }
        },
        VALID_MIN_TO_MAX(RESULT.OK) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.validMinToMax(ptn);
            }
        },
        VALID_MAX(RESULT.OK) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.validMax(ptn);
            }
        },
        INVALID_MIN(RESULT.ERROR) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.invalidMin(ptn);
            }
        },
        INVALID_MAX(RESULT.ERROR) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.invalidMax(ptn);
            }
        },
        INVALID_UNDER_MIN_LENGTH(RESULT.ERROR) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.invalidUnderMin(ptn);
            }
        },
        INVALID_OVER_MAX_LENGTH(RESULT.ERROR) {
            @Override
            public List<String> create(DataPattern ptn) {
                return AnalyzeUtil.invalidOverMax(ptn);
            }
        };
        private final RESULT result;
        private DATA_CREATOR(RESULT result){
            this.result = result;
        }
        public abstract List<String> create(DataPattern ptn);
        public String getPath(){
            return this.name() + "/";
        }
        public RESULT getResult(){
            return this.result;
        }
    }
    public static final DATA_CREATOR[] DATA_CREATORS = DATA_CREATOR.values();

    private AnalyzeUtil(){}

    public static List<String> validMin(DataPattern ptn) {
        List<String> result = new ArrayList<String>();

        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            if (targetSubPattern.getMin() == 0)
                continue;

            for (String value : targetSubPattern.getMinValid()) {
                result.add(getMinPtn(ptn, targetSubPattern, value));
            }
        }
        return result;
    }

    public static List<String> invalidUnderMin(DataPattern ptn) {
        List<String> result = new ArrayList<String>();
        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            if (targetSubPattern.getMin() == 0)
                continue;

            String value = targetSubPattern.getMinValid().get(0);
            result.add(getMinPtn(ptn, targetSubPattern, value.substring(0, targetSubPattern.getMin() - 1)));
        }
        return result;
    }

    public static List<String> invalidMin(DataPattern ptn) {
        List<String> result = new ArrayList<String>();
        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            if (targetSubPattern.getMin() == 0)
                continue;

            String valid = targetSubPattern.getMinValid().get(0);

            for (String value : targetSubPattern.getMinInvalid()) {
                String[] invalids = createInvalids(value, valid);
                for (String invalid : invalids) {
                    result.add(getMinPtn(ptn, targetSubPattern, invalid));
                }
            }
        }
        return result;
    }

    public static List<String> validMax(DataPattern ptn) {
        List<String> result = new ArrayList<String>();
        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            if (targetSubPattern.getMin() == targetSubPattern.getMax())
                continue;

            for (String value : targetSubPattern.getMaxValid()) {
                result.add(getMaxPtn(ptn, targetSubPattern, value));
            }
        }
        return result;
    }

    public static List<String> invalidMax(DataPattern ptn) {
        List<String> result = new ArrayList<String>();
        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            if (targetSubPattern.getMin() == targetSubPattern.getMax())
                continue;

            String valid = targetSubPattern.getMaxValid().get(0);

            for (String value : targetSubPattern.getMaxInvalid()) {
                String[] invalids = createInvalids(value, valid);
                for (String invalid : invalids) {
                    result.add(getMaxPtn(ptn, targetSubPattern, invalid));
                }
            }
        }
        return result;
    }

    public static List<String> invalidOverMax(DataPattern ptn) {
        List<String> result = new ArrayList<String>();
        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            String value = targetSubPattern.getMaxValid().get(0);
            result.add(getMaxPtn(ptn, targetSubPattern, value + value.substring(value.length() - 1)));
        }
        return result;
    }

     public static List<String> validMinToMax(DataPattern ptn) {
        List<String> result = new ArrayList<String>();
        for (SubPattern targetSubPattern : ptn.getSubPatterns()) {
            String value = targetSubPattern.getMaxValid().get(0);
            for (int i = targetSubPattern.getMin() + 1; i < targetSubPattern.getMax(); i++)
                result.add(getMinToMaxPtn(ptn, targetSubPattern, value.substring(0, i)));
        }
        return result;
    }

    private static String getMinPtn(DataPattern ptn, SubPattern targetSubPattern, String targetValue) {
        StringBuilder sb = new StringBuilder();
        for (SubPattern subPattern : ptn.getSubPatterns()) {
            if (subPattern == targetSubPattern)
                sb.append(targetValue);
            else
                sb.append(subPattern.getMinValid().get(0));
        }
        return sb.toString();
    }

    private static String getMinToMaxPtn(DataPattern ptn, SubPattern targetSubPattern, String targetValue) {
        StringBuilder sb = new StringBuilder();
        boolean afterSubPattern = false;
        for (SubPattern subPattern : ptn.getSubPatterns()) {
            if (subPattern == targetSubPattern){
                sb.append(targetValue);
                afterSubPattern = true;
            }else{
                if (afterSubPattern)
                    sb.append(subPattern.getMinValid().get(0));
                else
                    sb.append(subPattern.getMaxValid().get(subPattern.getMaxValid().size() - 1));
            }
        }
        return sb.toString();
    }


    private static String getMaxPtn(DataPattern ptn, SubPattern targetSubPattern, String targetValue) {
        StringBuilder sb = new StringBuilder();
        for (SubPattern subPattern : ptn.getSubPatterns()) {
            if (subPattern == targetSubPattern)
                sb.append(targetValue);
            else
                sb.append(subPattern.getMaxValid().get(subPattern.getMaxValid().size() - 1));
        }
        return sb.toString();
    }

    /**
     * 不正値のパターンとして、先頭が不正・中間が不正・末尾が不正の3パターンを作成
     *
     * @param invalid
     * @param valid
     * @return
     */
    private static String[] createInvalids(String invalid, String valid) {
        if (invalid.length() == 0)
            return BLANK;
        if (invalid.length() == 1)
            return new String[] { invalid };
        String startInvalid = invalid.substring(0, 1) + valid.substring(1);
        String endInvalid = valid.substring(0, valid.length() - 1) + invalid.substring(valid.length() - 1);
        if (invalid.length() == 2) {
            return new String[] { startInvalid, endInvalid };
        }
        String middleInvalid = valid.substring(0, 1) + invalid.substring(1, invalid.length() - 1)
                + valid.substring(valid.length() - 1);
        return new String[] { startInvalid, middleInvalid, endInvalid };
    }



}
