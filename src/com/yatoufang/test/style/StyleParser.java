package com.yatoufang.test.style;

import com.yatoufang.test.model.Element;

/**
 * @author GongHuang（hse）
 * @since 2022/3/29 0029
 */
public interface StyleParser {

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
