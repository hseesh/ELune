package com.yatoufang.test.style;

/**
 * @author GongHuang（hse）
 * @since 2021/12/23
 */
public enum NodeType {

    TOP_ROOT(1),
    ROOT(2),
    NORMAL(3),
    CONFIG_NODE(4),
    TABLE_NODE(5),
    PROTOCOL_NODE(6),
    CONTENT_NODE(7),
    PUSH_NODE(8),
    REQUEST_NODE(9),
    RESPONSE_NODE(10),
    CONFIG_KEY_NODE(11),
    OPERATION_TYPE_NODE(12),
    LOCK_TYPE_NODE(13),
    UNLOCK_TYPE_NODE(14),
    NONE(0);

    private final int ID;

    NodeType(int id) {
        this.ID = id;
    }

    public NodeType getType() {
        for (NodeType type : NodeType.values()) {
            if (type.getID() == ID) {
                return type;
            }
        }
        return NONE;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return String.valueOf(ID);
    }
}