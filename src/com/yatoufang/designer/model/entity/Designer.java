package com.yatoufang.designer.model.entity;

import com.google.common.collect.Maps;
import com.yatoufang.designer.model.Element;
import com.yatoufang.entity.Config;
import com.yatoufang.entity.Node;
import com.yatoufang.entity.Table;
import com.yatoufang.utils.StringUtil;

import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/5/3 0003
 */
public class Designer {

    private Node node;

    private Element element;

    private String tableContent;

    private Map<String, String> configContentMap;

    private Map<String, String> entityContentMap;

    private transient Table table;

    private transient Map<String, Config> configMap;


    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getTableContent() {
        return tableContent;
    }

    public void setTableContent(String tableContent) {
        this.tableContent = tableContent;
    }

    public Map<String, String> getConfigContentMap() {
        return configContentMap == null ? Maps.newHashMap() : configContentMap;
    }

    public void setConfigContentMap(Map<String, String> configContentMap) {
        this.configContentMap = configContentMap;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        table.setName(StringUtil.toCameCaseForSmallCameCase(table.getName()));
        this.table = table;
    }

    public Map<String, Config> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Config> configMap) {
        this.configMap = configMap;
    }

    public Map<String, String> getEntityContentMap() {
        return entityContentMap == null ? Maps.newHashMap() : entityContentMap;
    }

    public void setEntityContentMap(Map<String, String> entityContentMap) {
        this.entityContentMap = entityContentMap;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
