package com.yatoufang.complete.model.type;


/**
 * 配置类型
 *
 * @author hse
 * @since 2023-06-03 14:54:48
 */
public enum ConfigType {


    PARAM_CHECK(1),

    CREATE_DB(2),

    VARIABLE(3),

    CONTEXT(4),

    AUTO_FILL(5),

    NONE(0);

    private final int id;

    ConfigType(int id) {
        this.id = id;
    }

    public static ConfigType getType(int id) {
        for (ConfigType type : ConfigType.values()) {
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
