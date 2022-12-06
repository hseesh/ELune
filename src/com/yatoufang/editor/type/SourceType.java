package com.yatoufang.editor.type;

/**
 * @author hse
 * @since 2022/9/10 0010
 */
public enum SourceType {

    NONE(0),

    IN_PUT(1),

    OUT_PUT(2),

    CAME_TYPE(2);

    final int id;

    SourceType(int id) {
        this.id = id;
    }
}
