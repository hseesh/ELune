package com.yatoufang.test.event;

import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class WorldStateHandler extends EventHandler{

    @Override
    public void invoke(InputEvent event,EventType eventType) {
        if(Context.textAreaState.getAndSet(false)){
            Context.enableTextArea();
        }
        if(Context.menuState.getAndSet(false)){
            Context.enablePopMenu();
        }
        handler.invoke(event,eventType);
    }
}
