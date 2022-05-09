package com.yatoufang.test.event;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.model.Element;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class KeyBoardHandler extends EventHandler{

    @Override
    public void invoke(InputEvent event,EventType eventType) {
        if(event instanceof KeyEvent){
            switch (eventType){
                case INSERT_KEY:
                    Element element = Canvas.createElement(EditorContext.current);
                    EditorContext.setSelect(element);
                    Rectangle bounds = element.getBounds();
                    EditorContext.textArea.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
                    EditorContext.textAreaState.set(true);
                    EditorContext.pushUpdates(element);
                    EditorContext.refreshViewableRange(element.getBounds());
                    break;
                case DELETE_KEY:
                    EditorContext.removeCurrent();
                    break;
                case CTRL_S_KEY:
                    EditorContext.saveState.set(true);
                    EditorContext.save();
                    break;
                default:
                    EditorContext.tryUpdateText();
                    EditorContext.pushUpdates(EditorContext.current);
                    break;
            }
        }
        handler.invoke(event,eventType);
    }
}
