package com.senko.scash.pattern;

public interface ICheckChars {
    /** チェック対象 */
    enum CHECK{
        /** 配列全て */
        ALL,
        /** 配列の先頭と末尾 */
        FIRST_LAST
    }

    char[] HALF_NUMS          = {
            '0', '1','2', '3', '4', '5', '6', '7', '8', '9'};

    char[] HALF_UPPER_ALPHAS  = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'};

    char[] HALF_LOWER_ALPHAS  = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z'};

    char[] HALF_ASCII_SYMBOLS = {
            // 0x20(32) - 0x2f(47)
            ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
            // 0x3a(58) - 0x40(64)
            ':', ';', '<', '=', '>', '?', '@',
            // 0x5b(91) - 0x60(96)
            '[','\\', ']', '^', '_', '`',
            // 0x7b(123) - 0x7e(126)
            '{', '|', '}', '~'};

    char[] HALF_KANAS = {
            'ｦ', 'ｧ', 'ｨ', 'ｩ', 'ｪ', 'ｫ', 'ｬ', 'ｭ', 'ｮ', 'ｯ', 'ｰ', 'ｱ', 'ｲ',
            'ｳ', 'ｴ', 'ｵ', 'ｶ', 'ｷ', 'ｸ', 'ｹ', 'ｺ', 'ｻ', 'ｼ', 'ｽ', 'ｾ', 'ｿ',
            'ﾀ', 'ﾁ', 'ﾂ', 'ﾃ', 'ﾄ', 'ﾅ', 'ﾆ', 'ﾇ', 'ﾈ', 'ﾉ', 'ﾊ', 'ﾋ', 'ﾌ',
            'ﾍ', 'ﾎ', 'ﾏ', 'ﾐ', 'ﾑ', 'ﾒ', 'ﾓ', 'ﾔ', 'ﾕ', 'ﾖ', 'ﾗ', 'ﾘ', 'ﾙ',
            'ﾚ', 'ﾛ', 'ﾜ', 'ﾝ'};

    char[] HALF_KANA_SYMBOLS = {
            '｡', '｢', '｣', '､', '･',
            'ﾞ', 'ﾟ' };

//    enum CHECK_CHARS{
//        HALF_NUMS(CHECK.FIRST_LAST,ICheckChars.HALF_NUMS),
//        HALF_UPPER_ALPHAS(CHECK.FIRST_LAST,ICheckChars.HALF_UPPER_ALPHAS),
//        HALF_LOWER_ALPHAS(CHECK.FIRST_LAST,ICheckChars.HALF_LOWER_ALPHAS),
//        HALF_ASCII_SYMBOLS(CHECK.FIRST_LAST,ICheckChars.HALF_ASCII_SYMBOLS),
//        HALF_KANAS(CHECK.FIRST_LAST,ICheckChars.HALF_KANAS),
//        HALF_KANA_SYMBOLS(CHECK.FIRST_LAST,ICheckChars.HALF_KANA_SYMBOLS);
//        private final CHECK  check;
//        private final char[] chars;
//        private final char[] checkChars;
//        private CHECK_CHARS(CHECK check , char[] chars){
//            this.check = check;
//            this.chars = chars;
//            this.checkChars = this.check == CHECK.FIRST_LAST && chars.length > 1 ?
//                new char[]{this.chars[0],this.chars[this.chars.length - 1]}
//                : this.chars;
//        }
//        public char[] getNaked(){
//            return this.chars;
//        }
//        public char[] getCheckChars(){
//            return this.checkChars;
//        }
//    }

}
