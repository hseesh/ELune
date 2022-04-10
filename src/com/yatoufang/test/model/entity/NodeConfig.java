package com.yatoufang.test.model.entity;

import org.apache.commons.compress.utils.Lists;

import java.util.Collection;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class NodeConfig {

    private String name;

    private int type;

    private int layoutStyle;

    private String fileName;

    private int insertOrder;

    private String icon;

    private String expression;

    private final Collection<Integer> children = Lists.newArrayList();

    /**
     * red;
     * RED;
     * blue;
     * BLUE;
     * white;
     * WHITE;
     * black;
     * BLACK;
     * gray;
     * GRAY;
     * lightGray;
     * LIGHT_GRAY;
     * darkGray;
     * DARK_GRAY;
     * pink;
     * PINK;
     * orange;
     * ORANGE;
     * yellow;
     * YELLOW;
     * green;
     * GREEN;
     */
    private String linkLineColor;

    private String borderColor;

    private float scaleCoefficient;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getInsertOrder() {
        return insertOrder;
    }

    public void setInsertOrder(int insertOrder) {
        this.insertOrder = insertOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public String getLinkLineColor() {
        return linkLineColor;
    }

    public void setLinkLineColor(String linkLineColor) {
        this.linkLineColor = linkLineColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public float getScaleCoefficient() {
        return scaleCoefficient;
    }

    public void setScaleCoefficient(int scaleCoefficient) {
        this.scaleCoefficient = scaleCoefficient;
    }


    public void addChild(Integer config){
        children.add(config);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Integer> getChildren() {
        return children;
    }

    public int getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(int layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public void setScaleCoefficient(float scaleCoefficient) {
        this.scaleCoefficient = scaleCoefficient;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", fileName='" + fileName + '\'' +
                ", insertOrder=" + insertOrder +
                ", icon='" + icon + '\'' +
                ", expression='" + expression + '\'' +
                ", children=" + children +
                ", linkLineColor='" + linkLineColor + '\'' +
                ", borderColor='" + borderColor + '\'' +
                ", scaleCoefficient=" + scaleCoefficient +
                '}';
    }
}
