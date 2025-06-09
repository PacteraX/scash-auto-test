package com.senko.scash.relation;

import static com.senko.scash.Constants.PATH_SUB_RELATION;
import com.senko.scash.MainProperties;

public class SubProperties extends com.senko.scash.SubProperties {
    public SubProperties(MainProperties main, String name) {
        super(main, PATH_SUB_RELATION, name);
    }
}
