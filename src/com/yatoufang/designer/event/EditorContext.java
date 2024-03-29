package com.yatoufang.designer.event;

import com.google.gson.Gson;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiClass;
import com.yatoufang.action.TableScannerAction;
import com.yatoufang.designer.component.Canvas;
import com.yatoufang.designer.component.RootLayer;
import com.yatoufang.designer.draw.AbstractLayoutParser;
import com.yatoufang.designer.draw.LayoutContext;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.model.PopupMenuContext;
import com.yatoufang.designer.model.entity.Designer;
import com.yatoufang.entity.Node;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Application;
import com.yatoufang.utils.BuildUtil;
import com.yatoufang.utils.DataUtil;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class EditorContext {
    public static Element last;
    public static Element current;
    //indicates area taken up by graphics
    private static Point lastDraggingPoint;
    public static JTextArea textArea = new JTextArea();
    public static RootLayer rootPanel;
    public static JPopupMenu popupMenu = Canvas.createMenu();
    public static PopupMenuContext popupMenuContext = new PopupMenuContext();
    public static boolean clearMenuItemState = false;
    public static String filePath;
    public static Designer designer;
    public static AtomicBoolean menuState = new AtomicBoolean(false);
    public static AtomicBoolean saveState = new AtomicBoolean(true);
    public static AtomicBoolean shouldUpdate = new AtomicBoolean(true);
    public static AtomicBoolean textAreaState = new AtomicBoolean(false);
    public static AtomicBoolean draggingState = new AtomicBoolean(false);
    private static Document document;
    private static Dimension area;
    private static final Collection<Element> updates = Lists.newArrayList();

    public static void updateUI() {
        if (updates.size() > 0) {
            rootPanel.create(rootPanel.topic);
            shouldUpdate.set(false);
            saveState.set(true);
            updates.clear();
            save();
        }
        if (shouldUpdate.getAndSet(false)) {
            rootPanel.create(rootPanel.topic);
        }
    }

    public static void save() {
        if (saveState.get()) {
            WriteCommandAction.runWriteCommandAction(Application.project, () -> {
                Gson gson = new Gson();
                String content = gson.toJson(designer);
                if (content == null || content.length() == 0) {
                    return;
                }
                document.deleteString(0, document.getText().length());
                document.insertString(0, content);
                saveState.set(false);
                DataUtil.createTimer(2000, e -> {
                    saveState.set(true);
                });
            });
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
        if (lastDraggingPoint == null) {
            lastDraggingPoint = event.getPoint();
            return;
        }
        int offsetX = lastDraggingPoint.x - event.getX();
        int offsetY = lastDraggingPoint.y - event.getY();
        lastDraggingPoint = event.getPoint();
        if (offsetX == 0 && offsetY == 0) {
            return;
        }
    }

    public static void refreshViewableRange(Rectangle bounds) {
        boolean changed = false;
        Point point = bounds.getLocation();
        rootPanel.scrollRectToVisible(bounds);
        int this_width = (point.x + 200);
        if (this_width > area.width) {
            area.width = this_width;
            changed = true;
        }
        int this_height = (point.y + 200);
        if (this_height > area.height) {
            area.height = this_height;
            changed = true;
        }
        if (changed) {
            rootPanel.setPreferredSize(area);
            rootPanel.revalidate();
        }
    }

    public static void removeCurrent() {
        if (textArea.hasFocus()) {
            Element parent = current.parent;
            if (parent == null) {
                return;
            }
            parent.delete(current);
            setSelect(parent);
            textArea.setText(parent.text);
            textArea.setBounds(parent.getBounds());
            shouldUpdate.set(true);
        }
    }

    public static void reMeasure() {
        rootPanel.reMeasure();
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

    public static void setPreferredSize(int measuredWidth, int measuredHeight) {
        if (measuredWidth <= 0 && measuredHeight <= 0) {
            return;
        }
        area.setSize(measuredWidth, measuredHeight);
        rootPanel.setPreferredSize(area);
    }

    public static void addHorizontalOffset(int x, int selfWidth) {
        Canvas.setNodeOffset(x, selfWidth, 0, rootPanel.topic);
    }

    public static Element getRootElement() {
        return rootPanel.topic;
    }

    public static void refresh() {
        AbstractLayoutParser parser = LayoutContext.getParser(rootPanel.topic.layoutType);
        parser.onCreate(rootPanel.topic);
    }

    public static void setDesigner(Node node, List<String> methods){
        designer.setNode(node);
        current.children.clear();
        Canvas.createNodes(current, methods);
        refresh();
    }

    public static void setDesigner(String content) {
        designer.setTableContent(content);
        Table table = getTable(content);
        if (table == null) return;
        current.children.clear();
        Canvas.createNodes(current, table);
        refresh();
    }

    @Nullable
    private static Table getTable(String content) {
        PsiClass aClass = BuildUtil.createClass(content);
        if (aClass == null) {
            return null;
        }
        Table table = new TableScannerAction().getTable(aClass);
        if (table == null) {
            return null;
        }
        designer.setTable(table);
        return table;
    }

    public static void setDesigner(Map<String, String> fileMap) {
        designer.setConfigContentMap(fileMap);
        current.children.clear();
        fileMap.forEach((k, v) -> {
            Canvas.createNodes(current, k);
        });
        refresh();
    }

    public static void setEntities(Map<String, String> fileMap) {
        designer.setEntityContentMap(fileMap);
        current.children.clear();
        fileMap.forEach((k, v) -> {
            Canvas.createNodes(current, k);
        });
        refresh();
    }

    public static void setFilePath(String rootPath) {
        filePath = rootPath;
    }

    public static void setDocument(Document newDocument) {
        document = newDocument;
        if (document.getText().length() > 0) {
            try {
                System.out.println("init stage");
                Gson gson = new Gson();
                designer = gson.fromJson(document.getText(), Designer.class);
                Element element = designer.getElement();
                if (element != null) {
                    initialise(element);
                    rootPanel.topic = element;
                    if(designer.getTableContent() != null && !designer.getTableContent().isEmpty()){
                        getTable(designer.getTableContent());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("init designer");
            designer = new Designer();
        }
        area = new Dimension(0, 0);
        last = rootPanel.topic;
        current = rootPanel.topic;
        current.fillText(textArea, current.getBounds());
        designer.setElement(rootPanel.topic);
        rootPanel.init();
    }

    public static void initialise(Element element) {
        if (element == null) {
            return;
        }
        for (Element child : element.children) {
            child.parent = element;
            initialise(child);
        }
    }

    public static Point getCurrentPoint() {
        return current.getBounds().getLocation();
    }
}
