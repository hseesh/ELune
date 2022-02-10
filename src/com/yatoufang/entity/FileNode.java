package com.yatoufang.entity;


import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class FileNode extends DefaultMutableTreeNode {
    public String name;
    public String content;
    public boolean isCatalog;

    /**
     * Creates a tree node with no parent, no children, but which allows
     * children, and initializes it with the specified user object.
     *
     */
    public FileNode(String name, String content) {
        super(name);
        this.name = name;
        this.content = content;
        this.isCatalog = false;
    }

    /**
     * Creates a tree node that has no parent and no children, but which
     * allows children.
     */
    public FileNode(String name) {
        super(name);
        this.name = name;
        this.isCatalog = true;
    }

    public FileNode(String name, boolean flag) {
        super(name);
        this.name = name;
        this.isCatalog = flag;
    }
}
