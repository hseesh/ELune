package com.yatoufang.test.draw;


import com.yatoufang.test.model.Element;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/11
 */
public abstract class AbstractLayoutParser implements LayoutParser{

    protected  AbstractLayoutParser(){
        LayoutContext.register(getType(),this);
    }
    /**
     *  get element layout style type
     * @return {@link LayoutType}
     */
    public abstract LayoutType getType();

    public void parser(Element element, Graphics2D graphics){
    }
}
