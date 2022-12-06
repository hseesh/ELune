package com.yatoufang.editor.menus;


import com.yatoufang.editor.component.LinkLine;
import com.yatoufang.editor.constant.IconManager;
import icons.Icon;

import javax.swing.*;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class LinkLineMenu extends JPopupMenu {

    public LinkLineMenu(LinkLine cl) {
        JMenuItem menuItemDelete = new JMenuItem("Delete line", Icon.NODE_PUSH);
        menuItemDelete.addActionListener(e -> cl.getModel().deleteLine(cl));
        add(menuItemDelete);
    }

}