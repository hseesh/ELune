package com.yatoufang.test.event;

import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public abstract class EventHandler {

    protected EventHandler handler;

    public abstract void invoke(InputEvent event,EventType type);
}
