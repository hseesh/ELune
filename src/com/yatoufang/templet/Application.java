package com.yatoufang.templet;

import com.intellij.openapi.project.Project;

import java.text.DecimalFormat;


/**
 *
 * @author hse
 * @Date: 2021/2/4 0009
 */
public class Application {

    // 111   default value for response example
    private static final String[] WORDS = {"I", "love", "waking", "up", "in", "the", "morning", "not", "knowing", "what's", "gonna", "happen",
            "or", "who", "I'm", "gonna", "meet", "where", "I'm", "gonna", "wind", "up",
            "I", "can", "not", "even", "picture", "him", "at", "all", "He", "only", "live", "in", "my", "memory",
            "One", "may", "fall", "in", "love", "with", "many", "people", "during", "the", "lifetime", "When", "you", "finally", "get", "your", "own", "happiness", "you", "will", "understand",
            "the", "previous", "sadness", "is", "a", "kind", "of", "treasure", "which", "makes", "you", "better", "to", "hold", "and", "cherish", "the", "people", "you", "love",
            "I", "figure", "life", "is", "a", "gift", "and", "I", "don't", "intend", "on", "wasting", "it", "You", "never", "know", "what", "hand", "you're", "going", "to", "get",
            "dealt", "next", "You", "learn", "to", "take", "life", "as", "it", "comes", "at", "you"};

    //data type, for judging whether need to load class
    private static final String DATATYPE = "-int,Integer,long,Long,short,Short,double,Double,float,Float,boolean,Boolean,byte,Byte,char,Char,String,Object,BigDecimal,Number"+
                    "Collection,Vector,List,ArrayList,LinkedList,Set,HashSet,LinkedHashSet,TreeSet,Map,HashMap,TreeMap,"+
                    "Date,Datetime,Timestamp,Time,LocalDate,LocalDateTime";

    public static String PROJECT_ID = "com.yatoufang.hse.code.generator";


    public static Project project;

    public static String basePackage;


    public static String getWord() {
        return WORDS[(int) (Math.random() * 111)];
    }

    public static String getDecimal() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return String.valueOf(decimalFormat.format(Math.random() * 110));
    }


    public static String getInteger() {
        return String.valueOf((int) (Math.random() * 111));
    }

    public static boolean isBasicType(String data){
        data = data.replace("[]","");
        return DATATYPE.indexOf(data) > 0;
    }


    public static void setBasePackage(String basePackage) {
        if (basePackage.contains(".")) {
            String[] strings = basePackage.split("\\.");
            if (strings.length >= 2) {
                basePackage = strings[0] + "." + strings[1];
            }
        }
       Application.basePackage = basePackage;
    }


}
