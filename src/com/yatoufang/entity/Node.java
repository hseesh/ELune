package com.yatoufang.entity;

import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/6/22 0022
 */
public class Node {

    public String name;
    public String content;
    public boolean isCatalog;
    public boolean isInterface;
    public List<Node> children = Lists.newArrayList();

    public static Node valueOf(FileNode file){
        Node node = new Node();
        node.name = file.name;
        node.content = file.content;
        node.isCatalog = file.isCatalog;
        node.isInterface = file.isInterface;
        return node;
    }


    public void addChild(Node node){
        children.add(node);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCatalog() {
        return isCatalog;
    }

    public void setCatalog(boolean catalog) {
        isCatalog = catalog;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

}
