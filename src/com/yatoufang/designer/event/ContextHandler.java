package com.yatoufang.designer.event;

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
            EditorContext.setEditingNode(event);
        }
        handler.invoke(inputEvent,eventType);
    }
}
