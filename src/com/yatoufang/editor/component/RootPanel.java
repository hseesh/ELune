package com.yatoufang.editor.component;

import com.yatoufang.editor.Model;
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

    private RootCanvas rootCanvas;

    private SideBar sideBar;
    private ZoomPanel zoomPanel;
    private JScrollPane scrollPane;

    public RootPanel(Model model) {
        setLayout(new BorderLayout());
        rootCanvas = new RootCanvas(this, model);
        zoomPanel = new ZoomPanel(rootCanvas, WIDTH, HEIGHT);
        scrollPane = new JScrollPane(
                zoomPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        sideBar = new SideBar(rootCanvas);
        add(scrollPane, BorderLayout.CENTER);
        add(sideBar, BorderLayout.EAST);
    }

    public RootCanvas getDrawingSurface() {
        return rootCanvas;
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