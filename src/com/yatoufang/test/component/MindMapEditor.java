package com.yatoufang.test.component;

import com.yatoufang.test.adapter.MindMapController;

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

    public void reDrawBankGround(){
        mainView.updateUI();
    }

}
