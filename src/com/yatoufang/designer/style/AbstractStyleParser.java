package com.yatoufang.designer.style;

/**
 * @author GongHuang（hse）
 * @since 2022/5/1 0001
 */
public abstract class AbstractStyleParser implements LayoutStyleParser {

    protected AbstractStyleParser(){
        StyleContext.register(getType(),this);
    }

    public abstract NodeType getType();
}
