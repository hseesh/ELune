package com.yatoufang.test.component;

import com.intellij.ui.components.JBScrollPane;
import com.yatoufang.test.controller.MyKeyBoardAdapter;
import com.yatoufang.test.controller.MyMouseAdapter;
import com.yatoufang.test.controller.MyMouseMotionAdapter;
import com.yatoufang.test.event.EditorContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class MindMapEditor implements AdjustmentListener {

    private final JPanel rootPanel;


    public MindMapEditor() {
        MyMouseAdapter mouseAdapter = new MyMouseAdapter();
        MyMouseMotionAdapter mouseMotionAdapter = new MyMouseMotionAdapter();
        RootLayer rootLayer = EditorContext.rootPanel;
        rootLayer.addMouseListener(mouseAdapter);
        rootLayer.addMouseMotionListener(mouseMotionAdapter);
        Font font = new Font(null, Font.PLAIN, 25);

        EditorContext.textArea.setFont(font);
        EditorContext.textArea.addKeyListener(new MyKeyBoardAdapter());

        rootLayer.add(EditorContext.textArea);


        JScrollPane scrollPane = new JBScrollPane(rootLayer,JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(128);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(128);

        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setAutoscrolls(true);

        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public JComponent getComponent() {
        return rootPanel;
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return rootPanel;
    }

    /**
     * Invoked when the value of the adjustable has changed.
     *
     * @param e the event to be processed
     */
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        System.out.println("e = " + e);
        rootPanel.repaint();
    }
}
