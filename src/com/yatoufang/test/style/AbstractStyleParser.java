package com.yatoufang.test.style;

/**
 * @author GongHuang（hse）
 * @since 2022/3/29 0029
 */
public abstract class AbstractStyleParser implements StyleParser{

    protected AbstractStyleParser(){
        StyleContext.register(getType(),this);
    }

    public abstract NodeType getType();
}
