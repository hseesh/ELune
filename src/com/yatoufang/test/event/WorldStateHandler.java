package com.yatoufang.test.event;

import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class WorldStateHandler extends EventHandler{

    @Override
    public void invoke(InputEvent event,EventType eventType) {
        if(EditorContext.textAreaState.getAndSet(false)){
            EditorContext.enableTextArea();
        }
        if(EditorContext.menuState.getAndSet(false)){
            EditorContext.enablePopMenu();
        }
        handler.invoke(event,eventType);
    }
}
