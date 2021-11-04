package com.yatoufang.config;

import com.intellij.util.xml.DomFileDescription;
import com.yatoufang.xml.Mapper;


/**
 * @author hse
 * @date 2021/4/30 0030
 */
public class XmlDescription extends DomFileDescription {

    public XmlDescription() {
        super(Mapper.class, "mapper");
    }
}
