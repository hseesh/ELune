package com.yatoufang.test.model.entity;

import com.yatoufang.entity.Config;
import com.yatoufang.entity.Table;
import com.yatoufang.test.model.Element;

import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/5/3 0003
 */
public class Designer {

    private Element element;

    private String tableContent;

    private Map<String, String> configContentMap;

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
        return configContentMap;
    }

    public void setConfigContentMap(Map<String, String> configContentMap) {
        this.configContentMap = configContentMap;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Map<String, Config> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Config> configMap) {
        this.configMap = configMap;
    }
}
