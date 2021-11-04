package com.yatoufang.xml;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author hse
 * @date 2021/6/5 0005
 */
public interface Result extends DomElement {

    @Attribute("column")
    GenericAttributeValue<String> getColumn();

    @Attribute("property")
    GenericAttributeValue<String> getProperty();

    @Attribute("jdbcType")
    GenericAttributeValue<String> getJdbcType();
}
