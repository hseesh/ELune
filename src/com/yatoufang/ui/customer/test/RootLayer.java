package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.Crayons;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/30
 */
public class RootLayer extends JLayeredPane {

    Crayons brush;

    public RootLayer() {
        setSize(800, 600);
    }


    /**
     * Paints each of the components in this container.
     *
     * @param g the graphics context.
     * @see Component#paint
     * @see Component#paintAll
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D brush = (Graphics2D) g;
        this.brush = new Crayons(brush);
        Canvas.drawBankGround(brush);
    }
}
