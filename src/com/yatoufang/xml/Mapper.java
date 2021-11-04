package com.yatoufang.xml;

import com.intellij.util.xml.*;

import java.util.List;

/**
 * @author hse
 * @date 2021/5/30 0030
 */
public interface Mapper extends DomElement {
    @Attribute("namespace")
    GenericAttributeValue<String> getNamespace();

    @SubTagsList({"select", "insert", "update", "delete"})
    List<Base> getStatements();

    List<Select> getSelects();

    List<Insert> getInserts();

    List<Update> getUpdates();

    List<Delete> getDeletes();

    @SubTagList("resultMap")
    List<ResultMap> getResultMaps();



}
