package com.yatoufang.xml;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author hse
 * @date 2021/6/4 0004
 */
public interface ResultMap extends Base {

    @Attribute("type")
    GenericAttributeValue<String> getType();

    @SubTagList("result")
    List<Result> getColumnInfo();

    @SubTagList("id")
    List<Result> getTableId();
}
