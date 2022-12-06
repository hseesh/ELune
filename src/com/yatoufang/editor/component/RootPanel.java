package com.yatoufang.editor.component;

import com.yatoufang.editor.component.Canvas;
import com.yatoufang.editor.component.ZoomPanel;
import com.yatoufang.editor.menus.SideBar;

import javax.swing.*;
import java.awt.*;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    static final int WIDTH = 5000;
    static final int HEIGHT = 5000;

    private static com.yatoufang.editor.component.Canvas canvas;

    private SideBar sideBar;
    private ZoomPanel zoomPanel;
    private JScrollPane scrollPane;

    public RootPanel() {
        setLayout(new BorderLayout());
        canvas = new com.yatoufang.editor.component.Canvas(this);
        zoomPanel = new ZoomPanel(canvas, WIDTH, HEIGHT);
        scrollPane = new JScrollPane(
                zoomPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        disableArrowKeys(scrollPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
        sideBar = new SideBar(canvas);
        add(scrollPane, BorderLayout.CENTER);
        add(sideBar, BorderLayout.EAST);
    }

    private static void disableArrowKeys(InputMap inputMap) {
        String[] keystrokeNames = {"UP", "DOWN", "LEFT", "RIGHT",};
        for (String key : keystrokeNames) {
            inputMap.put(KeyStroke.getKeyStroke(key), "none");
        }
    }


    public static Canvas getDrawingSurface() {
        return canvas;
    }

    public ZoomPanel getZoomPanel() {
        return zoomPanel;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public SideBar getSideBar() {
        return sideBar;
    }
}