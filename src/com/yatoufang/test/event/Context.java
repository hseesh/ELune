package com.yatoufang.test.event;

import com.yatoufang.config.MindMapConfig;
import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.model.Element;
import com.yatoufang.ui.customer.test.RootLayer;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class Context {
    public static Element last;
    public static Element current;
    public static JTextArea textArea = new JTextArea();
    public static RootLayer rootPanel = new RootLayer();
    public static AtomicBoolean menuState = new AtomicBoolean(false);
    public static AtomicBoolean shouldUpdate = new AtomicBoolean(true);
    public static AtomicBoolean textAreaState = new AtomicBoolean(false);
    private static final Collection<Element> updates = Lists.newArrayList();

    static {
        last = rootPanel.topic;
        current = rootPanel.topic;
        current.fillText(textArea, current.getBounds());
    }

    public static void updateUI() {
        if (updates.size() > 0 || shouldUpdate.getAndSet(false)) {
            rootPanel.create(rootPanel.topic);
            updates.clear();
        }
    }

    public static void pushUpdates(Element element) {
        updates.add(element);
    }

    public static void setSelect(Element element) {
        last = current;
        current = element;
        last.fillText(textArea, last.getBounds());
        updates.add(last);
    }

    public static void enableTextArea() {
        Context.textArea.setVisible(true);
        Context.textArea.setEnabled(true);
        Context.textArea.requestFocus();
    }

    public static void addNode(Element element){
        last.add(element);
    }

    public static void enablePopMenu() {
    }

    public static void tryUpdateText() {
        if(Context.textArea.hasFocus()){
            current.fillText(textArea, current.getBounds());
            Canvas.setTextBounds(current);
        }
    }

    public static void setTextAreaBounds(Rectangle bounds) {
        textArea.setBounds(bounds);
    }
}
