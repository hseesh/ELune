package com.yatoufang.entity;

import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GongHuang（hse）
 * @since 2023/3/6
 */
public class EnumClass {

    private String name;
    private String alias;
    private String content;
    private String description;
    private List<Param> params = Lists.newArrayList();


    public static EnumClass valueOf(String alias, String content, String description, List<Param> params) {
        EnumClass model = new EnumClass();
        model.alias = alias;
        model.content = content;
        model.description = description;
        model.params = params;
        return model;
    }

    public void tryAddFields(Param field) {
        if (field == null) {
            return;
        }
        if (params.contains(field)) {
            params.remove(field);
        } else {
            field.setDefaultValue(String.valueOf(generatorId()));
            params.add(field);
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int generatorId() {
        List<String> collect = this.params.stream().map(Param::getDefaultValue).collect(Collectors.toList());
        int maxId = 0;
        for (String index : collect) {
            int value = Integer.parseInt(index);
            if (value > maxId) {
                maxId = value;
            }
        }
        return maxId;
    }
}
