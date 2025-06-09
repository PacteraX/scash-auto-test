package com.senko.scash.pattern;

public interface IValidPattern {
    boolean matchPatternName(String target);
    IValidInvalid getValidInvalid();

    enum DEFAULT_VALID_PATTERN implements IValidPattern{
        HALF_NUM(new IValidInvalid.HalfNum(), "半角数","半角数値","半角数字"),
        HALF_ALPHA(new IValidInvalid.HalfAlpha(), "半角英","半角英字","半角英文字"),
        HALF_LOWER_ALPHA(new IValidInvalid.HalfLowerAlpha(), "半角英小","半角英小文字"),
        HALF_UPPER_ALPHA(new IValidInvalid.HalfUpperAlpha(), "半角英大","半角英大文字"),
        HALF_ALPHA_NUM(new IValidInvalid.HalfAlphaNum(), "半角英数","半角英数値","半角英数字"),
        HALF_LOWER_ALPHA_NUM(new IValidInvalid.HalfLowerAlphaNum(), "半角英数小","半角英数小文字"),
        HALF_UPPER_ALPHA_NUM(new IValidInvalid.HalfUpperAlphaNum(), "半角英数大","半角英数大文字"),
        HALF_ALPHA_NUM_SYMBOL(new IValidInvalid.HalfAlphaNumSymbol(), "半角英数記号"),
        HALF_KANA(new IValidInvalid.HalfKana(), "半角ｶﾅ","半角カナ","半角かな"),
        INVALID(new IValidInvalid.Invalid(),"","入力値","入力値のみ","その他");
        private final String[] validNames;
        private final IValidInvalid validInvalid;
        private DEFAULT_VALID_PATTERN(IValidInvalid validInvalid,String... validNames){
            this.validInvalid = validInvalid;
            this.validNames = validNames;
        }
        public boolean matchPatternName(String target){
            for (String validName : validNames)
                if (validName.equals(target))
                    return true;
            return false;
        }
        public IValidInvalid getValidInvalid(){
            return this.validInvalid;
        }
    }

}
