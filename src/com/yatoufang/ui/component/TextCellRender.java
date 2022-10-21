package com.yatoufang.ui.component;

import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBColor;
import com.yatoufang.templet.Application;
import com.yatoufang.utils.StringUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/10/14
 */
public class TextCellRender extends EditorTextField implements ListCellRenderer<String> {

    public TextCellRender() {
        super(StringUtil.EMPTY, Application.project, JavaClassFileType.INSTANCE);
        setOneLineMode(false);
        setFont(new Font(null, Font.PLAIN, 14));
        setBorder(BorderFactory.createLineBorder(JBColor.BLACK));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value);
        if (isSelected) {
            setBorder(BorderFactory.createLineBorder(JBColor.BLUE, 4));
        } else {
            setBorder(BorderFactory.createLineBorder(JBColor.BLACK));
        }
        return this;
    }

}
