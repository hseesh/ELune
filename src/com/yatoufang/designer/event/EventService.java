package com.yatoufang.designer.event;

import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class EventService {

    private static final MouseHandler MOUSE_HANDLER = new MouseHandler();
    private static final UpdateHandler UPDATE_HANDLER = new UpdateHandler();
    private static final ContextHandler CONTEXT_HANDLER = new ContextHandler();
    private static final KeyBoardHandler KEY_BOARD_HANDLER = new KeyBoardHandler();
    private static final WorldStateHandler WORLD_STATE_HANDLER = new WorldStateHandler();

    static {
        CONTEXT_HANDLER.handler = MOUSE_HANDLER;
        MOUSE_HANDLER.handler = KEY_BOARD_HANDLER;
        KEY_BOARD_HANDLER.handler = UPDATE_HANDLER;
        UPDATE_HANDLER.handler = WORLD_STATE_HANDLER;
    }

    public static void post(InputEvent event, EventType eventType) {
        CONTEXT_HANDLER.invoke(event, eventType);
    }

}
