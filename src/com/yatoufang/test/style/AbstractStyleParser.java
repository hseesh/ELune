package com.yatoufang.test.style;

import com.yatoufang.test.model.Element;

/**
 * @author GongHuang（hse）
 * @since 2022/3/29 0029
 */
public abstract class AbstractStyleParser implements StyleParser{

    protected AbstractStyleParser(){
        StyleContext.register(getType(),this);
    }

    public abstract NodeType getType();

    /**
     * crate a element
     *
     * @param element element node
     */
    @Override
    public void onCreate(Element element) {

    }
}
