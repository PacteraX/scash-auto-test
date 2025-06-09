package com.senko.scash.pattern.cases;

import static com.senko.scash.Constants.PATH_SUB_PATTERN;
import com.senko.scash.MainProperties;

public class SubProperties extends com.senko.scash.SubProperties {
    public SubProperties(MainProperties main, String name) {
        super(main, PATH_SUB_PATTERN, name);
    }
}
