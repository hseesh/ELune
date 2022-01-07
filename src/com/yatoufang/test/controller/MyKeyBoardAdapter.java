package com.yatoufang.test.controller;


import com.yatoufang.test.event.EventService;
import com.yatoufang.test.event.EventType;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author GongHuang（hse）
 * @since 2022/1/6
 */
public class MyKeyBoardAdapter implements KeyListener {
    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key has been typed.
     * This event occurs when a key press is followed by a key release.
     *
     * @param e event
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_INSERT:
                System.out.println("insert type");
                EventService.post(e, EventType.INSERT_KEY);
                break;
            case KeyEvent.VK_DELETE:
                EventService.post(e, EventType.DELETE_KEY);
                break;
            case KeyEvent.VK_S:
                if(e.isControlDown() && e.getID() == KeyEvent.KEY_PRESSED){
                    EventService.post(e, EventType.CTRL_S_KEY);
                }
                break;
            default:
                break;
        }
    }
}
