package com.senko.scash;

public interface Constants {
    char   PATH_SEPARATOR                   = 47;

    String PATH_CONF                        = "conf/";
    String PATH_TEST_DATA                   = "test_data/";
    String PATH_IN_CASE                     = "case/";
    String PATH_OUT_PREDATA                 = "sql/";
    String PATH_OUT_REQUEST                 = "request/";
    String PATH_OUT_RESPONSE                = "response/";
    String PATH_OUT_RESPONSE_FILE           = "response/file/";
    String PATH_OUT_XML                     = "xml/";
    String PATH_OUT_RESULT                  = "result/";

    String PATH_SUB_SEPARATOR_PROPERTIES    = "file_templates/";
    String PATH_SUB_RELATION                = "relation/";
    String PATH_SUB_PATTERN                 = "pattern/";
    String FILE_SEPARATOR_PROPERTIES        = "separator.properties";

    int    RELATION_HEADER_START_ROW        = 1;
    String RELATION_HEADER_INPUT_SHEET_NAME = "input_sheet_name";
    String RELATION_HEADER_BEFORE           = "before";
    String RELATION_HEADER_BEFORE_SIM       = "before_sim";
    String RELATION_HEADER_ADJUST           = "adjust";
    String RELATION_HEADER_RESULT           = "result";
    String RELATION_HEADER_PREFIX           = "xml_prefix";
    String RELATION_HEADER_SUFFIX           = "xml_suffix";
    String RELATION_HEADER_SINGLE           = "single";
    String RELATION_HEADER_CLASS            = "class";
    String RELATION_SHEET_NAME              = "Relation";
    String HEADER_INPUT                     = "input";
    String HEADER_OUTPUT                    = "output";

    int TEST_CASE_COL   = 0;
    int METHOD_NAME_COL = 1;

}
