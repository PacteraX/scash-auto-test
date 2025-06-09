package com.senko.scash.relation;

import com.senko.scash.Constants;
import com.senko.scash.MainProperties;

public class Runner{

   public static void main(String[] args) throws Exception {
       if (args.length < 1)
           System.exit(1);

       MainProperties mainProps = new MainProperties(Constants.PATH_CONF + Constants.FILE_SEPARATOR_PROPERTIES);
       final Analyzer ana;
       if (args.length == 1){
           ana = new Analyzer(mainProps,new SubProperties(mainProps, args[0]));
       }else {
           ana = new ListAnalyzer(mainProps,new SubProperties(mainProps, args[0]));
       }

       ana.analyze();
       ana.createTestProperties();
   }

}
