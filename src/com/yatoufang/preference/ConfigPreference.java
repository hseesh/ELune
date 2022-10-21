package com.yatoufang.preference;

import com.yatoufang.entity.ConfigParam;

public interface ConfigPreference {


    void invoke(ConfigParam configParam);

    boolean check(String variable);


    ConfigParam getTemplate();

    String getExpression();


    String getName();


    String getKey();

    String ARRAY = "Array";

}
