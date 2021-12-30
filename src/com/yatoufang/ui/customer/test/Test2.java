package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.MainView;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/26 0026
 */
public class Test2 {

    public static void main(String[] args) {
        JFrame frame=new JFrame("MindMap");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RootLayer rootLayer = new RootLayer();
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.pink);
        textArea.setBounds(50,50,70,40);
        textArea.setText("hello text ");
        textArea.setVisible(true);
        rootLayer.add(textArea);
        RootLayerListener rootLayerListener = new RootLayerListener(rootLayer,textArea);
        rootLayer.addMouseListener(rootLayerListener);
        frame.getContentPane().add(rootLayer);
        frame.setVisible(true);
    }
}
