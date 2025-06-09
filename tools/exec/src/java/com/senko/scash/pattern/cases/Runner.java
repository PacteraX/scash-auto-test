package com.senko.scash.pattern.cases;

import static com.senko.scash.Constants.FILE_SEPARATOR_PROPERTIES;
import static com.senko.scash.Constants.PATH_CONF;

import com.senko.scash.MainProperties;

public class Runner {

    public static void main(String[] args) throws Exception{
        if (args.length < 1)
            System.exit(1);

        MainProperties mainProps = new MainProperties(PATH_CONF + FILE_SEPARATOR_PROPERTIES);

        Analyzer analyzer = new Analyzer(mainProps,new SubProperties(mainProps,args[0]));
        analyzer.createTestProperties();
        analyzer.analyze();
    }

}
