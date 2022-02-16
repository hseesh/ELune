package com.yatoufang.entity;


import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Objects;

/**
 * @author GongHuang（hse）
 * @since 2022/2/9
 */
public class FileNode extends DefaultMutableTreeNode {
    public String name;
    public String content;
    public boolean isCatalog;
    public String templatePath;
    public boolean isInterface;
    public Table table;

    /**
     * Creates a tree node that has no parent and no children, but which
     * allows children.
     */
    public FileNode(String name) {
        super(name);
        this.name = name;
        this.isCatalog = true;
    }

    /**
     * Creates a tree node with no parent, no children, but which allows
     * children, and initializes it with the specified user object.
     */
    public FileNode(String name, String templatePath) {
        super(name);
        this.name = name;
        this.templatePath = templatePath;
    }

    /**
     * Creates a tree node that has no parent and no children, but which
     * allows children.
     */
    public FileNode(String name, boolean flag) {
        super(name);
        this.name = name;
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

    public String getFilePath(String rootPath) {
        TreeNode[] path = super.getPath();
        StringBuilder builder = new StringBuilder(rootPath);
        for (TreeNode treeNode : path) {
            builder.append("\\").append(String.valueOf(treeNode));
        }
        return builder.toString();
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

}
