package com.yatoufang.designer.style;

import com.yatoufang.designer.model.Element;

/**
 * @author GongHuang（hse）
 * @since 2022/5/1 0001
 */
public abstract class AbstractNodeEventParser extends AbstractStyleParser implements EventParser{

    @Override
    public void preview() {

    }

    /**
     * crate a element
     *
     * @param element element node
     */
    @Override
    public void create(Element element) {

    }

    /**
     * crate a element
     *
     * @param element element node
     */
    @Override
    public void onCreate(Element element) {

    }
}
