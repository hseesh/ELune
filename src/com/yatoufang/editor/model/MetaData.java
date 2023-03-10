package com.yatoufang.editor.model;

import com.yatoufang.entity.Param;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author hse
 * @since 2022/9/11 0011
 */
public class MetaData {

    private String name;

    private String alias;

    private String description;

    private Collection<Param> pramList = Lists.newArrayList();

    public void setPramList(Collection<Param> pramList) {
        this.pramList = pramList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Param> getPramList() {
        return pramList;
    }

    public int calcOptimumWidth() {
        int length = 0;
        for (Param param : pramList) {
            length = Math.max(param.getPrimaryInfo().length() * 10, length);
        }
        return length;
    }

    public void setPramList(List<Param> pramList) {
        this.pramList = pramList;
    }

    public void tryAddFields(Param field) {
        if (field == null) {
            return;
        }
        if (pramList.contains(field)) {
            pramList.remove(field);
        } else {
            pramList.add(field);
        }
    }

    public void addFields(Collection<Param> fields) {
        for (Param field : fields) {
            tryAddFields(field);
        }
    }
    public Param getKey(){
        for (Param param : this.pramList) {
            if (int.class.getSimpleName().equals(param.getTypeAlias())) {
                return param;
            }
            if (long.class.getSimpleName().equals(param.getTypeAlias())) {
                return param;
            }
        }
        return null;
    }
}
