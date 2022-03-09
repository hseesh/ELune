package com.yatoufang.entity;


import com.yatoufang.utils.StringUtil;

/**
 * @author GongHuang（hse）
 * @since 2021/11/8 13:09
 */
public class Field extends Param {

    public Field(String paramName) {
        super(paramName);
        this.setDescription(StringUtil.EMPTY);
        this.setConstraint(" NOT NULL");
        this.setAnnotation("@Column");
    }

    public Field(String paramName, String type) {
        super(paramName);
        this.setTypeAlias(type);
        this.setDescription("更新时间");
        this.setConstraint(" NOT NULL");
        this.setAnnotation("@Column");
    }

    private String constraint;

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

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    @Override
    public String toString() {
        return "`" + this.getName() + "` " + this.getTypeAlias() +
                this.constraint + (isPrimaryKey || isForeignKey ? " PRIMARY KEY " : StringUtil.EMPTY) +
                (this.getDescription().isEmpty() ? "" : (" COMMENT '" + this.getDescription() + "'"));
    }
}
