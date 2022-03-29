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
                    switch (event.getButton()) {
                        case MouseEvent.BUTTON1:
                            EditorContext.textArea.setVisible(false);
                            EditorContext.textArea.setEnabled(false);
                            EditorContext.popupMenu.setVisible(false);
                            EditorContext.popupMenu.setEnabled(false);
                            EditorContext.current.fillText(EditorContext.textArea, EditorContext.textArea.getBounds());
                            break;
                        case MouseEvent.BUTTON3:
                            EditorContext.menuState.set(true);
                            EditorContext.enablePopMenu(event);
                            break;
                        default:
                            break;
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
