package com.yatoufang.entity;


import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2021/11/4 20:56
 */
public class Table {

    private String name;

    private String comment;

    private List<Field> fields;

    private Field primaryKey;

    private String valueOf;

    public Table(String trim) {
        this.name = trim.replace("\"","");
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


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(this.name).append("(\n");
        for (int i = 0; i < this.fields.size(); i++) {
            builder.append("    ").append(this.fields.get(i).toString()).append(i != this.fields.size() - 1 ? "," : "").append("\n");
        }
        builder.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='").append(this.comment).append("';");
        return builder.toString();
    }
}
