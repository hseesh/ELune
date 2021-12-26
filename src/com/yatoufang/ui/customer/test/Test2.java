package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.MainView;

import javax.swing.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/26 0026
 */
public class Test2 {

    public static void main(String[] args) {
        JFrame frame=new JFrame("MindMap");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainView());
        frame.setVisible(true);
    }
}
