package com.yatoufang.designer.style;

import com.yatoufang.designer.model.Element;

/**
 * @author GongHuang（hse）
 * @since 2022/3/29 0029
 */
public interface LayoutStyleParser {

    /**
     *  crate a element
     * @param element element node
     */
    void create(Element element);


    /**
     *  crate a element
     * @param element element node
     */
    void onCreate(Element element);
}
