package com.yatoufang.designer.controller;


import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.style.AbstractNodeEventParser;
import com.yatoufang.designer.style.AbstractStyleParser;
import com.yatoufang.designer.style.StyleContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author GongHuang（hse）
 * @since 2022/4/28
 */
public class PopMenuListener implements ActionListener {
    /**
     * Invoked when an action occurs.
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object sourceObject = event.getSource();
        if (sourceObject instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) sourceObject;
            AbstractStyleParser parser = StyleContext.getParser(EditorContext.current.type);
            if (!(parser instanceof AbstractNodeEventParser)) {
                return;
            }
            AbstractNodeEventParser eventParser = (AbstractNodeEventParser) parser;
            switch (menuItem.getText()) {
                case "Preview current":
                    eventParser.preview();
                    break;
                case "Edit current":
                    eventParser.onExecute();
                    break;
                default:
                    break;
            }
        }

    }
}
