package com.yatoufang.test.event;

import com.yatoufang.test.model.Element;

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
            Element result = EditorContext.rootPanel.topic.find(point);
            if(result != null){
                EditorContext.setSelect(result);
                EditorContext.textArea.setText(result.text);
                EditorContext.textArea.setBounds(result.getBounds());
                EditorContext.textAreaState.set(true);
                EditorContext.pushUpdates(result);
            }
        }
        handler.invoke(inputEvent,eventType);
    }
}
