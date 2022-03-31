package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.RootLayer;
import com.yatoufang.test.controller.MyKeyBoardAdapter;
import com.yatoufang.test.controller.MyMouseAdapter;
import com.yatoufang.test.controller.MyMouseMotionAdapter;
import com.yatoufang.test.event.EditorContext;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/26 0026
 */
public class Test2 {

    public static void main(String[] args) {
        JFrame frame = new JFrame("MindMap");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RootLayer rootLayer = EditorContext.rootPanel;
        rootLayer.add(EditorContext.textArea);
        EditorContext.textArea.setFont(new Font(null, Font.PLAIN, 25));
        EditorContext.textArea.addKeyListener(new MyKeyBoardAdapter());
        MyMouseAdapter mouseAdapter = new MyMouseAdapter();
        MyMouseMotionAdapter mouseMotionAdapter = new MyMouseMotionAdapter();
        rootLayer.addMouseListener(mouseAdapter);
        rootLayer.addMouseMotionListener(mouseMotionAdapter);
        frame.getContentPane().add(rootLayer);
        frame.setVisible(true);
    }
}
