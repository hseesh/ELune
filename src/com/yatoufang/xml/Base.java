package com.yatoufang.xml;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author hse
 * @date 2021/5/30 0030
 */
public interface Base extends DomElement {

    @Attribute("id")
    GenericAttributeValue<String> getId();

    @Attribute("parameterType")
    GenericAttributeValue<String> getParameterType();

    @Attribute("statementType")
    GenericAttributeValue<String> getStatementType();

    @Attribute("resultType")
    GenericAttributeValue<String> getResultType();

    @Attribute("resultMap")
    GenericAttributeValue<String> getResultMap();

}
