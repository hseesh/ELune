package com.yatoufang.ui.customer.test;

import com.yatoufang.test.controller.MyKeyBoardAdapter;
import com.yatoufang.test.controller.MyMouseAdapter;
import com.yatoufang.test.event.Context;

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
        RootLayer rootLayer = Context.rootPanel;
        rootLayer.add(Context.textArea);
        Context.textArea.setFont(new Font(null, Font.PLAIN, 25));
        Context.textArea.addKeyListener(new MyKeyBoardAdapter());
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        rootLayer.addMouseListener(myMouseAdapter);
        frame.getContentPane().add(rootLayer);
        frame.setVisible(true);
    }
}
