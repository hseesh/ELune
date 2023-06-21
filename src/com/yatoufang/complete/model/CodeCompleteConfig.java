package com.yatoufang.complete.model;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class CodeCompleteConfig {

    private int id;

    private int modelType;

    private Map<String, String> templateMaps = Maps.newHashMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public Map<String, String> getTemplateMaps() {
        return templateMaps;
    }

    public void setTemplateMaps(Map<String, String> templateMaps) {
        this.templateMaps = templateMaps;
    }

    public String getTemplate(String key) {
        return templateMaps.get(key);
    }
}
