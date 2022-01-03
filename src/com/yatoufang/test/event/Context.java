package com.yatoufang.test.event;

import com.yatoufang.test.model.AbstractElement;
import com.yatoufang.ui.customer.test.RootLayer;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class Context {
    public static AbstractElement last;
    public static AbstractElement current;
    public static RootLayer panel = new RootLayer();
    public static JTextArea textArea = new JTextArea();
    public static AtomicBoolean menuState = new AtomicBoolean(false);
    public static AtomicBoolean textAreaState = new AtomicBoolean(false);
    private static final Collection<AbstractElement> updates = Lists.newArrayList();

    public static void updateUI() {
        if(updates.size() > 0){
            panel.create(panel.topic);
            updates.clear();
        }
    }

    public static void pushUpdates(AbstractElement element){
        updates.add(element);
    }

    public static void setSelect(AbstractElement element){
        last = current;
        current = element;
    }

    public static void enableTextArea(){
        Context.textArea.setVisible(true);
        Context.textArea.setEnabled(true);
        Context.textArea.requestFocus();
    }

    public static void enablePopMenu() {
    }
}
