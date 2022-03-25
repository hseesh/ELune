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
                            Context.textArea.setVisible(false);
                            Context.textArea.setEnabled(false);
                            Context.popupMenu.setVisible(false);
                            Context.popupMenu.setEnabled(false);
                            Context.current.fillText(Context.textArea, Context.textArea.getBounds());
                            break;
                        case MouseEvent.BUTTON3:
                            Context.menuState.set(true);
                            Context.enablePopMenu(event);
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
