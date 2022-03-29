package com.yatoufang.test.component;

import com.intellij.ui.components.JBScrollPane;
import com.yatoufang.test.controller.MyKeyBoardAdapter;
import com.yatoufang.test.controller.MyMouseAdapter;
import com.yatoufang.test.event.EditorContext;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class MindMapEditor {

    private final JPanel rootPanel;
    private final JScrollPane scrollPane;


    public MindMapEditor() {
        rootPanel = new JPanel(new BorderLayout());
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        RootLayer rootLayer = EditorContext.rootPanel;
        rootLayer.setMinimumSize(new Dimension(1920, 1080));
        rootLayer.addMouseListener(myMouseAdapter);
        Font font = new Font(null, Font.PLAIN, 25);

        EditorContext.textArea.setFont(font);
        EditorContext.textArea.addKeyListener(new MyKeyBoardAdapter());

        rootPanel.add(rootLayer);
        rootLayer.add(EditorContext.textArea);

        scrollPane = new JBScrollPane(rootPanel);
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

}
