package com.yatoufang.entity;

import com.yatoufang.utils.StringUtil;

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
        this.content = StringUtil.EMPTY;
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

    public FileNode(String name, boolean isCatalog,boolean isInterface) {
        super(name);
        this.name = name;
        this.isCatalog = isCatalog;
        this.isInterface = isInterface;
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
            builder.append("/").append(treeNode);
        }
        return builder.toString();
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public static FileNode valueOf(Node node){
        FileNode fileNode = new FileNode(node.name);
        fileNode.content = node.content;
        fileNode.isCatalog = node.isCatalog;
        fileNode.isInterface = node.isInterface;
        return fileNode;
    }

}
