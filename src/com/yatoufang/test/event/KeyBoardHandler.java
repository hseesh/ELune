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
                    Element element = Canvas.createElement(Context.current);
                    Context.setSelect(element);
                    Context.addNode(element);
                    Context.textArea.setBounds(element.getBounds().x, element.getBounds().y, 70, 40);
                    Context.textAreaState.set(true);
                    Context.pushUpdates(element);
                    break;
                case DELETE_KEY:
                    break;
                default:
                    Context.tryUpdateText();
                    Context.pushUpdates(Context.current);
                    break;
            }
        }
        handler.invoke(event,eventType);
    }
}
