package com.yatoufang.entity.complete;

import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2023/4/1
 */
public class SerializeLayer {

    private String name;

    private final List<String> names = new ArrayList<>();
    private final List<String> types = new ArrayList<>();

    private String lastKey;
    private String description;
    private boolean isCollection;

    public List<String> getNames() {
        return names;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public boolean isCollection() {
        return isCollection && names.size() > 1;
    }

    public void addName(String key) {
        names.add(key);
    }

    public void addTypes(String type) {
        types.add(type);
    }

    public boolean isEmpty() {
        return names.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecommendName() {
        return (getLastKey() == null ? ProjectKeys.ID : getLastKey()) + StringUtil.LEFT_ROUND_BRACKET + StringUtil.RIGHT_ROUND_BRACKET;
    }

    public String getLastElement() {
        if (names.isEmpty()) {
            return StringUtil.EMPTY;
        } else {
            return names.get(names.size() - 1);
        }
    }

    public String getPenultimateElement() {
        if (names.isEmpty()) {
            return StringUtil.EMPTY;
        } else {
            int index = Math.max((names.size() - 2), 0);
            return names.get(index);
        }
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

    @Override
    public String toString() {
        return "SerializeLayer{" +
                "names=" + names +
                ", types=" + types +
                ", description='" + description + '\'' +
                ", isCollection=" + isCollection +
                '}';
    }
}
