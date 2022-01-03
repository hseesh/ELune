package com.yatoufang.ui.customer.test;

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
        RootLayer rootLayer = Context.panel;
        rootLayer.add(Context.textArea);
        Context.textArea.setFont(new Font(null, Font.PLAIN, 25));
        RootLayerListener rootLayerListener = new RootLayerListener();
        rootLayer.addMouseListener(rootLayerListener);
        frame.getContentPane().add(rootLayer);
        frame.setVisible(true);
    }
}
