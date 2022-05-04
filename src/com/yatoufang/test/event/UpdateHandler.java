package com.yatoufang.test.event;


import com.yatoufang.test.style.AbstractNodeEventParser;
import com.yatoufang.test.style.AbstractStyleParser;
import com.yatoufang.test.style.StyleContext;

import javax.swing.*;
import java.awt.event.InputEvent;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class UpdateHandler extends EventHandler {

    @Override
    public void invoke(InputEvent event, EventType eventType) {
        if (EditorContext.menuState.get() && EditorContext.textAreaState.get()) {
            AbstractStyleParser styleParser = StyleContext.getParser(EditorContext.current.type);
            if (styleParser instanceof AbstractNodeEventParser) {
                AbstractNodeEventParser parser = (AbstractNodeEventParser) styleParser;
                enableEditMenuItem(parser.isSuperNode());
                EditorContext.clearMenuItemState = true;
            } else {
                if(EditorContext.clearMenuItemState){
                    enableEditMenuItem(false);
                    EditorContext.clearMenuItemState = false;
                }
            }
        } else {
            if(EditorContext.clearMenuItemState){
                enableEditMenuItem(false);
                EditorContext.clearMenuItemState = false;
            }
        }
        handler.invoke(event, eventType);
    }

    private void enableEditMenuItem(boolean state) {
        for (MenuElement subElement : EditorContext.popupMenu.getSubElements()) {
            JMenuItem menuItem = (JMenuItem) subElement;
            if ("Edit current".equals(menuItem.getText())) {
                menuItem.setVisible(state);
                break;
            }
        }
    }
}
