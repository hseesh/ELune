package com.yatoufang.test.event;

import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class EventService {

    private static final MouseHandler mouseHandler = new MouseHandler();
    private static final UpdateHandler updateHandler = new UpdateHandler();
    private static final ContextHandler contextHandler = new ContextHandler();
    private static final KeyBoardHandler keyBoardHandler = new KeyBoardHandler();
    private static final WorldStateHandler worldStateHandler = new WorldStateHandler();

    static {
        contextHandler.handler = mouseHandler;
        mouseHandler.handler = keyBoardHandler;
        keyBoardHandler.handler = worldStateHandler;
        worldStateHandler.handler = updateHandler;
    }

    public static void post(InputEvent event,EventType eventType){
        contextHandler.invoke(event,eventType);
    }

}
