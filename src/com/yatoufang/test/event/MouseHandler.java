package com.yatoufang.test.event;

import com.yatoufang.test.model.RootElement;
import com.yatoufang.utils.StringUtil;

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
                        case 3:
                            System.out.println(event.getX() + " " + event.getY());
                            RootElement rootElement = new RootElement(StringUtil.EMPTY);
                            rootElement.setBounds(event.getX(), event.getY(), 70, 40);
                            Context.textArea.setBounds(event.getX(), event.getY(), 70, 40);
                            Context.textAreaState.set(true);
                            Context.panel.topic.add(rootElement);
                            Context.pushUpdates(rootElement);
                            break;
                        case 1:
                            Context.textArea.setVisible(false);
                            Context.textArea.setEnabled(false);
                            break;
                        default:break;
                    }
                    break;
                case MOUSE_DRAG:
                    break;
                case MOUSE_MOVE:
                    break;
                default:break;

            }
        }
        handler.invoke(inputEvent, eventType);
    }
}
