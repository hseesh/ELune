package com.yatoufang.test.event;

/**
 * @author GongHuang（hse）
 * @since 2022/1/3 0003
 */
public enum EventType {
    MOUSE_CLICK(1),
    MOUSE_DRAG(2),
    MOUSE_MOVE(3),
    TYPING(4),
    INSERT_KEY(5),
    DELETE_KEY(6),
    CTRL_S_KEY(7),
    NONE(10);


    int ID;

    EventType(int type) {
        ID = type;
    }

    public int getType(EventType eventType) {
        for (EventType value : EventType.values()) {
            if (value == eventType) {
                return value.ID;
            }
        }
        return NONE.ID;
    }
}
