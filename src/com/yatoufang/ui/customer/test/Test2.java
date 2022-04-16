package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.MindMapEditor;
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
        MindMapEditor mindMapEditor = new MindMapEditor();
        frame.getContentPane().add(mindMapEditor.getComponent());
        frame.setVisible(true);
    }
}
