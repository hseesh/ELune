package com.yatoufang.test.component;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class MainView extends JComponent {

    private Crayons crayons;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D brush  = (Graphics2D) g.create();
        Canvas.drawBankGround(brush);
    }



}
