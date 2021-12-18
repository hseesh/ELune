package com.yatoufang.entity;


import com.yatoufang.templet.DBQueueType;
import com.yatoufang.utils.StringUtil;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2021/11/4 20:56
 */
public class Table {

    private String name;

    private String alias;

    private String comment;

    private List<Field> fields;

    private Field primaryKey;

    private String valueOf;

    private String workSpace;

    private DBQueueType dbQueueType;

    private boolean multiEntity;

    public Table(String name, String type) {
        this.name = name.replace("\"",StringUtil.EMPTY);
        int indexOf = type.indexOf(".");
        if(indexOf >= 0){
            type = type.substring(indexOf);
            switch (type){
                case "NONE":
                    this.dbQueueType = DBQueueType.NONE;
                    break;
                case "DEFAULT":
                    this.dbQueueType = DBQueueType.DEFAULT;
                    break;
                default:
                    this.dbQueueType = DBQueueType.IMPORTANT;
                    break;
            }
        }
        this.alias = StringUtil.toUpperCaseWithUnderLine(this.name);
    }

    public boolean isMultiEntity() {
        return multiEntity;
    }

    public void setMultiEntity(boolean multiEntity) {
        this.multiEntity = multiEntity;
    }

    public Field getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Field primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getValueOf() {
        return valueOf;
    }

    public void setValueOf(String valueOf) {
        this.valueOf = valueOf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public DBQueueType getDbQueueType() {
        return dbQueueType;
    }

    public void setDbQueueType(DBQueueType dbQueueType) {
        this.dbQueueType = dbQueueType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(this.name).append("(\n");
        for (int i = 0; i < this.fields.size(); i++) {
            builder.append("    ").append(this.fields.get(i).toString()).append(i != this.fields.size() - 1 ? "," : "").append("\n");
        }
        builder.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='").append(this.comment).append("';\n");
        return builder.toString();
    }

    public void tryAddFields(Field field) {
        if(field == null){
            return;
        }
        if(fields.contains(field)){
            fields.remove(field);
        }else{
            fields.add(field);
        }
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
    }

    public void removePrimaryField(String name) {
        fields.removeIf(e -> e.getName().equals(name));
    }

    public void addFields(Field object) {
        fields.add(object);
    }

}
