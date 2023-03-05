package com.yatoufang.editor.type;


import java.util.Collection;
import java.util.List;

/**
 * @author hse
 * @since 2022/9/11 0011
 */
public enum NodeType {

    NONE(0),

    ENTITY_NODE(1),

    DATA_BASE(2),

    NORMAL_CONFIG(3),


    ENUM_CONFIG(4),

    REQUEST_NODE(5),

    RESPONSE_NODE(6),

    RUSH_NODE(7),

    PROTOCOL_NODE(8);

    final int id;

    NodeType(int id) {
        this.id = id;
    }

    public static final Collection<NodeType> AUTO_WRITE = List.of(REQUEST_NODE, RESPONSE_NODE);

    public static final Collection<NodeType> AUTO_TRANSLATE = List.of(DATA_BASE, PROTOCOL_NODE, ENTITY_NODE);


    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
