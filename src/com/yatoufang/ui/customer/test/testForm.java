package com.yatoufang.ui.customer.test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class testForm {
    private JButton button1;
    private JPanel panel1;
    private JTree tree1;

    public testForm() {
        button1.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
