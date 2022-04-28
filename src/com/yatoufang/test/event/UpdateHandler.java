package com.yatoufang.test.event;


import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class UpdateHandler extends EventHandler{

    @Override
    public void invoke(InputEvent event,EventType eventType) {
        handler.invoke(event, eventType);
    }
}
