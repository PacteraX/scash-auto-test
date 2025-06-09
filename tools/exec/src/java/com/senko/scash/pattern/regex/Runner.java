package com.senko.scash.pattern.regex;

public class Runner {

    public static void main(String[] args) throws Exception{
        if (args.length < 1)
            System.exit(1);

        Analyzer analyzer = new Analyzer();
        analyzer.analyze(args[0]);
    }

}
