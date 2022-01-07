package com.yatoufang.test.event;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class MouseHandler extends EventHandler {

    @Override
    public void invoke(InputEvent inputEvent, EventType eventType) {
        if (inputEvent instanceof MouseEvent) {
            MouseEvent event = (MouseEvent) inputEvent;
            switch (eventType) {
                case MOUSE_CLICK:
                    if (event.getButton() == 1) {
                        Context.textArea.setVisible(false);
                        Context.textArea.setEnabled(false);
                    }
                    break;
                case MOUSE_DRAG:
                    break;
                case MOUSE_MOVE:
                    break;
                default:
                    break;

            }
        }
        handler.invoke(inputEvent, eventType);
    }
}
