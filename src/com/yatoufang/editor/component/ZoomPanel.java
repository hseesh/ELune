package com.yatoufang.editor.component;

import com.yatoufang.editor.event.EventDispatch;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.SpringLayout.Constraints;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class ZoomPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AffineTransform transform;
    private final SpringLayout layout;
    private final JPanel springPanel;
    private Container view;

    private static final double zoomFactor = 0.1;

    public ZoomPanel(Container view, int width, int height) {
        this(view);
        setPreferredSize(new Dimension(width, height));
    }

    private ZoomPanel(Container view) {
        setLayout(new BorderLayout());
        this.view = view;
        transform = new AffineTransform();
        layout = new SpringLayout();
        springPanel = new JPanel(layout);
        if (view != null) {
            updateConstraints();
            springPanel.add(view);
        }
        EventDispatch layerUI = new EventDispatch();
        layerUI.setTransform(transform);
        JLayer<JComponent> layer = new JLayer<>(springPanel, layerUI);

        super.add(layer);
    }

    public double getScale() {
        return transform.getScaleX();
    }

    public void zoomIn() {
        setScale(transform.getScaleX() + zoomFactor);
    }

    public void zoomOut() {
        setScale(transform.getScaleX() - zoomFactor);
    }

    private void updateConstraints() {
        Spring width = layout.getConstraint(SpringLayout.WIDTH, springPanel);
        Spring height = layout.getConstraint(SpringLayout.HEIGHT, springPanel);

        Constraints constraints = layout.getConstraints(view);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(0));
        constraints.setWidth(Spring.scale(width, (float) (1 / transform.getScaleX())));
        constraints.setHeight(Spring.scale(height, (float) (1 / transform.getScaleX())));
    }

    private void setView(Container view) {
        if (this.view != null) {
            throw new IllegalStateException(this.getClass().getName() + " cannot be shared between multiple containers");
        }
        if (view == null) {
            throw new IllegalArgumentException("Can't set a null view");
        }
        this.view = view;
        updateConstraints();
        springPanel.add(view);
    }

    public void setScale(double scale) {
        final double minValue = 0.3;
        if (scale >= minValue) {
            transform.setToIdentity();
            transform.scale(scale, scale);
            updateConstraints();
            springPanel.updateUI();
        }
    }

    private Component addToView(Component comp, Object constraints, int index) {
        if (view != null) {
            view.add(comp, constraints, index);
            return comp;
        }
        if (comp instanceof Container) {
            setView((Container) comp);
            return view;
        }
        throw new IllegalStateException("You need to add or set a Container view before adding Components");
    }

    @Override
    public Component add(Component comp) {
        return addToView(comp, null, this.getComponentCount());
    }

    @Override
    public Component add(Component comp, int index) {
        return addToView(comp, null, index);
    }

    @Override
    public void add(@NotNull Component comp, Object constraints) {
        addToView(comp, constraints, this.getComponentCount());
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        addToView(comp, constraints, index);
    }
}