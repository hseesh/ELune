package com.yatoufang.test.event;

import com.yatoufang.test.component.Canvas;
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
                    if(EditorContext.draggingState.get()){
                        return;
                    }
                    Element result = EditorContext.setEditingNode(event);
                    if (result == null) {
                        EditorContext.setViewPoint(event);
                        return;
                    }
                    disableInput();
                    Rectangle bounds = result.getBounds();
                    Rectangle newBounds = Canvas.calcTopLeftPoint(event.getPoint(),bounds);
                    result.setBounds(newBounds);
                    EditorContext.textArea.setBounds(newBounds);
                    EditorContext.pushUpdates(result);
                    enableDraggingState();
                    break;
                case MOUSE_MOVE:
                    break;
                default:
                    break;
            }
        }
        handler.invoke(inputEvent, eventType);
    }

    private void enableDraggingState() {
        EditorContext.draggingState.set(true);
        DataUtil.createTimer(10, e -> {
            EditorContext.draggingState.set(false);
        }).start();
    }

    private void disableInput() {
        EditorContext.textArea.setVisible(false);
        EditorContext.textArea.setEnabled(false);
        EditorContext.popupMenu.setVisible(false);
        EditorContext.popupMenu.setEnabled(false);
        EditorContext.current.fillText(EditorContext.textArea, EditorContext.textArea.getBounds());
    }
}
