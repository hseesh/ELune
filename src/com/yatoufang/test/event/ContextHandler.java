package com.yatoufang.test.event;

import com.yatoufang.test.model.AbstractElement;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class ContextHandler extends EventHandler{

    @Override
    public void invoke(InputEvent inputEvent,EventType eventType) {
        if (inputEvent instanceof MouseEvent) {
            MouseEvent event = (MouseEvent) inputEvent;
            Point point = event.getPoint();
            AbstractElement result = Context.panel.topic.find(point);
            if(result != null){
                Context.setSelect(result);
                Context.textArea.setBounds(result.getBounds());
                Context.textAreaState.set(true);
            }
        }
        handler.invoke(inputEvent,eventType);
    }
}
