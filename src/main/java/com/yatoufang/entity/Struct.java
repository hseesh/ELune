package com.yatoufang.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2022/8/19
 */
public class Struct {

    private final Map<String, String> fields = Maps.newLinkedHashMap();

    private final List<String> order = Lists.newArrayList();

    private String fileName;

    private String value;

    private String name;

    public Struct() {
    }

    public Struct(String name) {
        this.name = name;
    }

    public void addFields(String key, String value) {
        fields.put(key, value);
    }

    public void addOrder(String field) {
        order.add(field);
    }

    public List<String> getFieldsValues() {
        return Lists.newArrayList(fields.values());
    }

    public String getOrder(int index) {
        return order.get(index);
    }

    public List<String> getFieldsKeys() {
        return Lists.newArrayList(fields.keySet());
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public List<String> getOrder() {
        return order;
    }

    public String getLastKey() {
        return getKeyByIndex(fields.size() - 1);
    }

    public String getLastValue() {
        if (order.size() == 0) {
            return StringUtil.EMPTY;
        }
        return order.get(order.size() - 1);
    }

    public String getPenultima() {
        if (order.isEmpty() || order.size() < 2) {
            return StringUtil.EMPTY;
        }
        return order.get(order.size() - 2);
    }

    public String getReturnExpression() {
        if (Collection.class.getSimpleName().equals(getLastKey())) {
            return getInfo(fields.size() - 1);
        }
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value.trim();
    }

    public String getInfo(int index) {
        if (this.getFields().size() < index) {
            return StringUtil.EMPTY;
        }
        List<String> types = Lists.newArrayList(this.getFields().keySet());
        List<String> values = Lists.newArrayList(this.getFields().values());
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < types.size(); i++) {
            String type = types.get(i);
            builder.append(type).append(StringUtil.LESS_THEN);
            if (type.contains(Map.class.getSimpleName())) {
                builder.append(values.get(i));
                builder.append(StringUtil.COMMA);
            }
        }
        builder.append(getValue());
        builder.append(String.valueOf(StringUtil.GRATE_THEN).repeat(Math.max(0, types.size() - index)));
        return builder.toString();
    }

    public String getRecommendName(int index) {
        if (this.getFields().size() <= index) {
            return StringUtil.EMPTY;
        }
        List<String> types = Lists.newArrayList(this.getFields().keySet());
        String type = types.get(index);
        switch (type.trim()) {
            case "Map":
            case "TreeMap":
                return order.get(index) + "KeyMap";
            case "List":
            case "Collection":
            case "Set":
                return order.get(index) + "Collection";
            default:
                return type;
        }
    }


    public String getKeyByIndex(int index) {
        List<String> types = Lists.newArrayList(this.getFields().keySet());
        return types.get(index);
    }

    public String getComment() {
        StringBuilder builder = new StringBuilder("通过");
        for (String s : order) {
            builder.append(s);
            builder.append(StringUtil.SPACE);
        }
        builder.append("获取 ").append(fileName);
        return builder.toString();
    }


    /**
     * 获取
     */
    public String getValue(String key) {
        return fields.get(key);
    }


    @Override
    public String toString() {
        return "Struct{" + "fields=" + fields + ", order=" + order + ", fileName='" + fileName + '\'' + ", value='" + value + '\'' + ", name='" + name + '\'' + '}';
    }
}
