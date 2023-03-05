package com.yatoufang.editor.model;

import com.intellij.psi.PsiClass;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.BuildUtil;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author hse
 * @since 2022/9/11 0011
 */
public class NodeData implements Serializable {

    public static final long serialVersionUID = 123L;
    private String name;
    private String alias;
    private String extra;
    private String content;
    private NodeType nodeType;
    private String workingSpace;
    private transient MetaData metaData;

    public NodeData(NodeType nodeType) {
        this.nodeType = nodeType;
        this.name = StringUtil.EMPTY;
        this.alias = StringUtil.EMPTY;
        this.metaData = new MetaData();
        this.workingSpace = ProjectKeys.GAME_SERVER;
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

    public int getTitleWidth(){
        return alias.length() * 10;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public String getContent() {
        return content == null ? StringUtil.EMPTY : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getWorkingSpace() {
        return workingSpace;
    }

    public void setWorkingSpace(String workingSpace) {
        this.workingSpace = workingSpace;
    }

    public void refreshWorkingSpace(String workingSpace){
        this.workingSpace = workingSpace;
    }


    public void reload(){
        this.metaData = new MetaData();
        refresh(this.content);
    }

    public void refresh(String content) {
        PsiClass aClass = BuildUtil.createClass(content);
        if (aClass == null) {
            return;
        }
        String description = PSIUtil.getDescription(aClass.getDocComment());
        List<Param> list = PSIUtil.getClassAllFields(aClass);
        this.metaData.setDescription(description);
        this.metaData.setName(aClass.getName());
        this.metaData.addFields(list);
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeData)) return false;
        NodeData nodeData = (NodeData) o;
        return Objects.equals(name, nodeData.name) && nodeType == nodeData.nodeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nodeType);
    }
}
