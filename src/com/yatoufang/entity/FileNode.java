package com.yatoufang.entity;


import com.google.common.collect.Lists;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Objects;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class FileNode extends DefaultMutableTreeNode {
    public String name;
    public String alias;
    public final List<Field> fields = Lists.newArrayList();
    public boolean isCatalog;
    public String templatePath;
    private Field primaryKey;

    /**
     * Creates a tree node that has no parent and no children, but which
     * allows children.
     */
    public FileNode(String name) {
        super(name);
        this.name = name;
        this.alias = name;
        this.isCatalog = true;
    }

    /**
     * Creates a tree node with no parent, no children, but which allows
     * children, and initializes it with the specified user object.
     */
    public FileNode(String name, String templatePath) {
        super(name);
        this.name = name;
        this.alias = name;
        this.templatePath = templatePath;
    }

    /**
     * Creates a tree node that has no parent and no children, but which
     * allows children.
     */
    public FileNode(String name, boolean flag) {
        super(name);
        this.name = name;
        this.alias = name;
        this.isCatalog = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileNode fileNode = (FileNode) o;
        return Objects.equals(name, fileNode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public boolean tryAddFields(Field field) {
        if(field == null){
            return false;
        }
        if(fields.contains(field)){
            fields.remove(field);
        }else{
            fields.add(field);
        }
        return true;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}
