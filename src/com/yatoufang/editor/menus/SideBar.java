package com.yatoufang.editor.menus;

import com.yatoufang.editor.component.RootCanvas;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class SideBar extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel zoomFactor = new JLabel();

    public SideBar(RootCanvas rootCanvas) {
        setPreferredSize(new Dimension(40, getPreferredSize().height));
        updateZoomLabel(rootCanvas);
        zoomFactor.setFont(new Font("Serif", Font.PLAIN, 10));
        add(zoomFactor);
    }

    public void updateZoomLabel(RootCanvas rootCanvas) {
        double percent = rootCanvas.getRootPanel().getZoomPanel().getScale() * 100;
        zoomFactor.setText(new DecimalFormat("#").format(percent) + "%");
    }

}