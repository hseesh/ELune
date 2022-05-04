package com.yatoufang.test.controller;

import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.style.AbstractNodeEventParser;
import com.yatoufang.test.style.StyleContext;

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
            switch (menuItem.getText()){
                case "Preview current":
                    AbstractNodeEventParser click = (AbstractNodeEventParser) StyleContext.getParser(EditorContext.current.type);
                    if(click == null){
                        return;
                    }
                    click.preview();
                    break;
                case "Edit current":
                    AbstractNodeEventParser execute = (AbstractNodeEventParser) StyleContext.getParser(EditorContext.current.type);
                    if(execute == null){
                        return;
                    }
                    execute.onExecute();
                    break;
                default:break;
            }
        }

    }
}
