package com.yatoufang.test.event;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.model.Element;

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
                    EditorContext.addNode(element);
                    EditorContext.textArea.setBounds(element.getBounds().x, element.getBounds().y, 70, 40);
                    EditorContext.textAreaState.set(true);
                    EditorContext.pushUpdates(element);
                    break;
                case DELETE_KEY:
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
