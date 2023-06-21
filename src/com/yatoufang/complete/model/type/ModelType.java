package com.yatoufang.complete.model.type;


/**
 * 模型类型
 *
 * @author hse
 * @since 2023-06-03 14:54:48
 */
public enum ModelType {


    STARTING_POINT(1),
    VARIABLE_MODEL(2),

    DEEP_LEARNING(3),

    CONTEXT(4),
    FILL_ARGUMENT(4),

    NONE(0);

    private final int id;

    ModelType(int id) {
        this.id = id;
    }

    public static ModelType getType(int id) {
        for (ModelType type : ModelType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return NONE;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
