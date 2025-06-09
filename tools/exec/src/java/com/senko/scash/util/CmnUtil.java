package com.senko.scash.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CmnUtil {

    public static void mkdir(String path){
        File dir = new File(path);
        if (dir.exists()){
            if (delete(dir));
                System.out.println(" << DELETE DIR : " + path);
        }

        dir.mkdirs();
        System.out.println(" >> CREATE DIR : " + path);
    }
    private static boolean delete(File parent){
        if (parent.getAbsolutePath().indexOf("test_data") < 0)
            throw new RuntimeException("test_data配下でないファイルまたはディレクトリを削除できないようにオンコーディングしています。それ以外の箇所の削除が必要な方はこの例外が発生している箇所を見直して下さい。");

            for (File chiled : parent.listFiles())
                if (chiled.isDirectory())
                    delete(chiled);
                else
                    chiled.delete();
            return parent.delete();
    }
    public static String getPath(String path) {
        return !path.endsWith("/") ? path + "/" : path;
    }

    public static String removeDoubleQuate(String val) {

        if (val.startsWith("\"")) {
            val = val.substring(1, val.length());
        }
        if (val.endsWith("\"")) {
            val = val.substring(0, val.length() - 1);
        }
        return val;
    }


    public static String trimChar(String data, String target) {
        int endIndex = data.length() - 1;
        if (data.indexOf(target) == 0 && data.indexOf(target, endIndex) == endIndex) {
            return data.substring(1, endIndex);
        } else {
            return data;
        }
    }

    public static String unicodeEscape(String value) {

        if (value == null)
            return "";

        char[] charValue = value.toCharArray();

        StringBuilder result = new StringBuilder();
        for (char ch : charValue) {
            if (ch != '_' && !(ch >= '0' && '9' >= ch) && !(ch >= 'a' && 'z' >= ch) && !(ch >= 'A' && 'Z' >= ch)) {
                String unicodeCh = Integer.toHexString(ch);

                result.append("\\u");
                for (int i = 0; i < 4 - unicodeCh.length(); i++) {
                    result.append("0");
                }
                result.append(unicodeCh);

            } else {
                result.append(ch);
            }

        }

        return result.toString();
    }

    public static boolean isNull(String value){
        return value == null || value.equals("");
    }

    public static void loadProperties(String path,final Properties properties){
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
