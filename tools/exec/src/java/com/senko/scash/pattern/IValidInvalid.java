package com.senko.scash.pattern;

import static com.senko.scash.pattern.ICheckChars.HALF_ASCII_SYMBOLS;
import static com.senko.scash.pattern.ICheckChars.HALF_KANAS;
import static com.senko.scash.pattern.ICheckChars.HALF_KANA_SYMBOLS;
import static com.senko.scash.pattern.ICheckChars.HALF_LOWER_ALPHAS;
import static com.senko.scash.pattern.ICheckChars.HALF_NUMS;
import static com.senko.scash.pattern.ICheckChars.HALF_UPPER_ALPHAS;

import java.util.HashSet;
import java.util.Set;
public interface IValidInvalid {
    char[][] valid();
    char[][] invalid();

    /** 半角英数字 ASCII範囲の記号 半角カナ 半角カナ範囲の記号 */
    char[][] DEFAULT_INVALID = {HALF_LOWER_ALPHAS,HALF_UPPER_ALPHAS,HALF_NUMS,HALF_ASCII_SYMBOLS,HALF_KANAS,HALF_KANA_SYMBOLS};

    abstract class ValidInvalid implements IValidInvalid {
        private final char[][] valid;
        private final char[][] invalid;
        public ValidInvalid(char[][] valid){
            this.valid = valid;
            Set<char[]> invalidTemp = new HashSet<char[]>();
            INV : for (char[] i : DEFAULT_INVALID){
                for(char[] v : valid){
                    if (v == i)
                        continue INV;
                }
                invalidTemp.add(i);
            }
            this.invalid = new char[invalidTemp.size()][];
            invalidTemp.toArray(this.invalid);
        }
        public ValidInvalid(char[][] valid ,char[][] invalid){
            this.valid    = valid;
            this.invalid  = invalid;
        }

        public char[][] valid() {
            return valid.clone();
        }

        public char[][] invalid() {
            return invalid.clone();
        }
    }

    class HalfNum extends ValidInvalid {
        private static final char[][] valid   = {HALF_NUMS};
        public HalfNum() {
            super(valid);
        }
    }

    class HalfAlpha extends ValidInvalid {
        private static final char[][] valid   = {HALF_UPPER_ALPHAS,HALF_LOWER_ALPHAS};
        public HalfAlpha() {
            super(valid);
        }
    }

    class HalfUpperAlpha extends ValidInvalid{
        private static final char[][] valid   = {HALF_UPPER_ALPHAS};
        public HalfUpperAlpha() {
            super(valid);
        }
    }

    class HalfLowerAlpha extends ValidInvalid{
        private static final char[][] valid   = {HALF_LOWER_ALPHAS};
        public HalfLowerAlpha() {
            super(valid);
        }
    }

    class HalfAlphaNum extends ValidInvalid{
        private static final char[][] valid   = {HALF_UPPER_ALPHAS,HALF_LOWER_ALPHAS,HALF_NUMS};
        public HalfAlphaNum() {
            super(valid);
        }
    }

    class HalfUpperAlphaNum extends ValidInvalid{
        private static final char[][] valid   = {HALF_UPPER_ALPHAS,HALF_NUMS};
        public HalfUpperAlphaNum() {
            super(valid);
        }
    }

    class HalfLowerAlphaNum extends ValidInvalid{
        private static final char[][] valid   = {HALF_LOWER_ALPHAS,HALF_NUMS};
        public HalfLowerAlphaNum() {
            super(valid);
        }
    }

    class HalfAlphaNumSymbol extends ValidInvalid{
        private static final char[][] valid   = {HALF_LOWER_ALPHAS,HALF_UPPER_ALPHAS,HALF_NUMS,HALF_ASCII_SYMBOLS};
        public HalfAlphaNumSymbol() {
            super(valid);
        }
    }

    class HalfKana extends ValidInvalid{
        private static final char[][] valid   = {HALF_KANAS,HALF_KANA_SYMBOLS};
        public HalfKana() {
            super(valid);
        }
    }

    class Invalid extends ValidInvalid{
        private static final char[][] valid   = {};
        public Invalid() {
            super(valid);
        }
    }
}
