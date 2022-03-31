package com.yatoufang.test.event;

import com.yatoufang.test.model.Element;
import com.yatoufang.utils.DataUtil;

import java.awt.*;
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
                            disableInput();
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
                    if (!EditorContext.draggingState.get()) {
                        Element result = EditorContext.setEditingNode(event);
                        if (result == null) {
                            return;
                        }
                        disableInput();
                        Rectangle bounds = result.getBounds();
                        Rectangle newBounds = new Rectangle(event.getX(), event.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
                        result.setBounds(newBounds);
                        System.out.println("newBounds = " + newBounds);
                        EditorContext.textArea.setBounds(newBounds);
                        EditorContext.pushUpdates(result);
                        EditorContext.draggingState.set(true);
                        DataUtil.createTimer(10, e -> {
                            EditorContext.draggingState.set(false);
                        }).start();
                    } else {
                        return;
                    }
                    break;
                case MOUSE_MOVE:
                    break;
                default:
                    break;

            }
        }
        handler.invoke(inputEvent, eventType);
    }

    private void disableInput() {
        EditorContext.textArea.setVisible(false);
        EditorContext.textArea.setEnabled(false);
        EditorContext.popupMenu.setVisible(false);
        EditorContext.popupMenu.setEnabled(false);
        EditorContext.current.fillText(EditorContext.textArea, EditorContext.textArea.getBounds());
    }
}
