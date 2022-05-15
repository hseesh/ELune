package com.yatoufang.test.style;

import com.yatoufang.test.model.Element;

/**
 * @author GongHuang（hse）
 * @since 2022/5/1 0001
 */
public interface EventParser {

    void preview();

    void onPreview(Element node);


    void onExecute();


    boolean isSuperNode();
}
