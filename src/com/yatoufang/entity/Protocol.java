package com.yatoufang.entity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/6/25
 */
public class Protocol {

    private String name;

    private String alias;

    private String comment;

    private List<TcpMethod> methods = Lists.newArrayList();

    public static Protocol valueOf(String name, String alias, String comment, List<TcpMethod> methods) {
        Protocol model = new Protocol();
        model.name = name;
        model.alias = alias;
        model.comment = comment;
        model.methods = methods;
        return model;
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

    public List<TcpMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<TcpMethod> methods) {
        this.methods = methods;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
