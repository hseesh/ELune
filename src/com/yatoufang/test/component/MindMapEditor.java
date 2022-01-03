package com.yatoufang.test.component;

import com.yatoufang.test.controller.MindMapController;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class MindMapEditor {

    private JPanel rootPanel;
    private MainView mainView;
    private JScrollPane scrollPane;


    public MindMapEditor() {
        init();
    }

    private void init() {
        mainView = new MainView();
        MindMapController mindMapController = new MindMapController(this);
        mainView.addMouseListener(mindMapController);
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(mainView);
        scrollPane = new JScrollPane(rootPanel);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(128);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(128);

        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setAutoscrolls(true);
    }

    public JComponent getComponent() {
        return scrollPane;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return scrollPane;
    }

    public void reDrawBankGround() {
        mainView.updateUI();
    }

    public MainView getMainView() {
        return mainView;
    }
}
