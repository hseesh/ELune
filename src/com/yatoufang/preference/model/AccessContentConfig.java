package com.yatoufang.preference.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/10/18
 */
public class AccessContentConfig {

    private int type;

    private List<String> templates = Lists.newArrayList();


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }
}
