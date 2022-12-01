package com.yatoufang.designer.style;

import com.yatoufang.designer.model.Element;

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
