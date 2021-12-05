package com.yatoufang.entity;


/**
 * @author GongHuang（hse）
 * @since 2021/11/8 13:09
 */
public class Field extends Param{

    public Field(String paramName) {
        super(paramName);
        this.setDescription("");
    }

    public Field(String paramName, String type){
        super(paramName);
        this.setTypeAlias(type);
        this.setDescription("更新时间");
    }

    private boolean isPrimaryKey;

    private boolean isForeignKey;

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(String tag) {
        isPrimaryKey = Boolean.parseBoolean(tag);
    }

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public void setForeignKey(String tag) {
        isForeignKey = Boolean.parseBoolean(tag);
    }

    @Override
    public String toString() {
        return   this.getName() + " " + this.getTypeAlias() +
                (this.getDefaultValue().isEmpty() ? "" : (" DEFAULT VALUE "  + getDefaultValue()) ) +
                (isPrimaryKey ?  " PRIMARY KEY AUTO_INCREMENT " : "") +
                (this.getDescription().isEmpty() ? "" : (" COMMENT '" + this.getDescription() + "'"));
    }
}
