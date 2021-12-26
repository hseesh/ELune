package com.yatoufang.test.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public class Topic {
    private String text;
    private Topic parent;
    private Map<String,Object> attributes = Maps.newHashMap();
    private  List<Topic> children = Lists.newArrayList();
    private AbstractElement element;

    public Topic(String name) {
        text = name;
        element = new RootElement(name);
    }

    public int[] getPositionPath() {
        final Topic[] path = getPath();
        final int[] result = new int[path.length];

        Topic current = path[0];
        int index = 1;
        while (index < path.length) {
            final Topic next = path[index];
            final int theindex = current.children.indexOf(next);
            result[index++] = theindex;
            if (theindex < 0) {
                break;
            }
            current = next;
        }

        return result;
    }

    public Topic[] getPath() {
        final List<Topic> list = new ArrayList<>();
        Topic current = this;
        do {
            list.add(0, current);
            current = current.parent;
        }
        while (current != null);
        return list.toArray(new Topic[0]);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Topic getParent() {
        return parent;
    }

    public void setParent(Topic parent) {
        this.parent = parent;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Topic> getChildren() {
        return children;
    }

    public void setChildren(List<Topic> children) {
        this.children = children;
    }

    public AbstractElement getElement() {
        return element;
    }

    public void setElement(AbstractElement element) {
        this.element = element;
    }
}
