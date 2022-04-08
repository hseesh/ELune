package com.yatoufang.test.event;

import com.yatoufang.test.component.Canvas;
import com.yatoufang.test.component.RootLayer;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.model.PopupMenuContext;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class EditorContext {
    public static Element last;
    public static Element current;
    private static JScrollPane mainView;
    private static Point lastDraggingPoint;
    public static JTextArea textArea = new JTextArea();
    public static RootLayer rootPanel = new RootLayer();
    public static JPopupMenu popupMenu = Canvas.createMenu();
    public static PopupMenuContext popupMenuContext = new PopupMenuContext();
    public static AtomicBoolean menuState = new AtomicBoolean(false);
    public static AtomicBoolean shouldUpdate = new AtomicBoolean(true);
    public static AtomicBoolean textAreaState = new AtomicBoolean(false);
    public static AtomicBoolean draggingState = new AtomicBoolean(false);
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

    public static void addNode(Element element) {
        last.add(element);
    }

    public static void enableTextArea() {
        EditorContext.textArea.setVisible(true);
        EditorContext.textArea.setEnabled(true);
        EditorContext.textArea.requestFocus();
    }

    public static void enablePopMenu() {
        popupMenu.show(popupMenuContext.getInvoke(), popupMenuContext.getX(), popupMenuContext.getY());
    }

    public static void enablePopMenu(MouseEvent event) {
        popupMenuContext.setX(event.getX());
        popupMenuContext.setY(event.getY());
        popupMenuContext.setInvoke(event.getComponent());
    }

    public static void tryUpdateText() {
        if (EditorContext.textArea.hasFocus()) {
            current.fillText(textArea, current.getBounds());
            Canvas.setTextBounds(current);
        }
    }

    public static Element setEditingNode(MouseEvent event) {
        Point point = event.getPoint();
        Element result = EditorContext.rootPanel.topic.find(point);
        if (result != null) {
            EditorContext.setSelect(result);
            EditorContext.textArea.setText(result.text);
            EditorContext.textArea.setBounds(result.getBounds());
            EditorContext.textAreaState.set(true);
            EditorContext.pushUpdates(result);
        }
        return result;
    }

    public static void setTextAreaBounds(Rectangle bounds) {
        textArea.setBounds(bounds);
    }

    public static void setViewPoint(MouseEvent event) {
        if (mainView == null) {
            return;
        }
        if(lastDraggingPoint == null){
            lastDraggingPoint = event.getPoint();
            return;
        }
        int offsetX = lastDraggingPoint.x - event.getX() ;
        int offsetY = lastDraggingPoint.y - event.getY() ;
        lastDraggingPoint = event.getPoint();
        if(offsetX == 0 && offsetY == 0){
            return;
        }
        JViewport viewport = mainView.getViewport();
        Rectangle viewRect = viewport.getViewRect();
        viewRect.setLocation(1900,1900);
        rootPanel.scrollRectToVisible(viewRect);
        mainView.revalidate();
        mainView.repaint();
    }

    public static void bindingView(JScrollPane scrollPane) {
        mainView = scrollPane;
    }

    public static void removeCurrent() {
        if (textArea.hasFocus()) {
            Element parent = current.parent;
            if (parent == null) {
                return;
            }
            parent.delete(current);
            setSelect(parent);
            textArea.setBounds(parent.getBounds());
            shouldUpdate.set(true);
        }
    }

    public static boolean couldDragging(Point point, Rectangle bounds) {
        if (lastDraggingPoint == null) {
            return false;
        }
        if (bounds.contains(lastDraggingPoint)) {
            return true;
        }
        EditorContext.lastDraggingPoint = point;
        return false;
    }
}