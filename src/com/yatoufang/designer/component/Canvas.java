package com.yatoufang.designer.component;

import com.intellij.ui.JBColor;
import com.yatoufang.config.MindMapConfig;
import com.yatoufang.designer.controller.PopMenuListener;
import com.yatoufang.designer.draw.LayoutType;
import com.yatoufang.designer.event.EditorContext;
import com.yatoufang.designer.model.Element;
import com.yatoufang.designer.style.NodeType;
import com.yatoufang.entity.Table;
import com.yatoufang.utils.StringUtil;
import icons.Icon;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class Canvas {

    private static final AffineTransform AFFINE_TRANSFORM = new AffineTransform();

    private static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(AFFINE_TRANSFORM, true, true);

    public static Point calcBestPosition(String text, Font font, Rectangle2D bounds) {
        int width = (int) font.getStringBounds(text, FONT_RENDER_CONTEXT).getWidth();
        int height = (int) font.getStringBounds(text, FONT_RENDER_CONTEXT).getHeight();
        double x = (bounds.getWidth() - width) / 2;
        double y = bounds.getHeight() - (int) (height / 2);
        return new Point((int) (bounds.getX() + x), (int) (bounds.getY() + y));
    }

    public static void setTextBounds(Element element) {
        int width = (int) element.font.getStringBounds(element.text, FONT_RENDER_CONTEXT).getWidth();
        int height = (int) element.font.getStringBounds(element.text, FONT_RENDER_CONTEXT).getHeight();
        Rectangle bounds = element.getBounds();
        if (width > bounds.width * 2 / 3) {
            element.setBounds(bounds.x, bounds.y, bounds.width + 25, bounds.height);
            EditorContext.setTextAreaBounds(element.getBounds());
        }
    }

    public static void setElementBounds(Element element) {
        int width = (int) element.font.getStringBounds(element.text, FONT_RENDER_CONTEXT).getWidth();
        int height = (int) element.font.getStringBounds(element.text, FONT_RENDER_CONTEXT).getHeight();
        Rectangle bounds = element.getBounds();
        if (width > bounds.width) {
            element.setBounds(bounds.x, bounds.y, width + 25, bounds.height);
        }
        if (height > bounds.height) {
            element.setBounds(bounds.x, bounds.y, width, bounds.height + 10);
        }
    }

    public static void drawBankGround(Graphics brush) {
        Rectangle clipBounds = brush.getClipBounds();

        brush.setColor(new JBColor(new Color(0x617B94), new Color(0x617B94)));
        brush.drawRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

        final double scaledGridStep = 32;

        final float minX = clipBounds.x;
        final float minY = clipBounds.y;
        final float maxX = clipBounds.x + clipBounds.width;
        final float maxY = clipBounds.y + clipBounds.height;

        brush.setColor(JBColor.BLACK);

        for (float x = 0.0f; x < maxX; x += scaledGridStep) {
            if (x < minX) {
                continue;
            }
            final int x1 = Math.round(x);
            brush.drawLine(x1, (int) minY, x1, (int) maxY);
        }

        for (float y = 0.0f; y < maxY; y += scaledGridStep) {
            if (y < minY) {
                continue;
            }
            final int y1 = Math.round(y);
            brush.drawLine((int) minX, y1, (int) maxX, y1);
        }
    }

    public static JPopupMenu createMenu() {
        JPopupMenu menu = new JPopupMenu();
        PopMenuListener listener = new PopMenuListener();
        JMenuItem edit = new JMenuItem("Edit current", Icon.EDITOR);
        JMenuItem previewCurrent = new JMenuItem("Preview current", Icon.VIEW);
        JMenuItem previewALL = new JMenuItem("Preview current", Icon.VIEW_ALL);
        JMenuItem shortCut = new JMenuItem("Short cut", Icon.SHORT_CUT);
        JMenuItem airBlower = new JMenuItem("Add Child", Icon.ADD_CHILD);
        previewCurrent.addActionListener(listener);
        shortCut.addActionListener(listener);
        airBlower.addActionListener(listener);
        edit.addActionListener(listener);
        edit.setVisible(false);
        menu.add(edit);
        menu.addSeparator();
        menu.add(previewCurrent);
        menu.addSeparator();
        menu.add(previewALL);
        menu.addSeparator();
        menu.add(airBlower);
        menu.addSeparator();
        menu.add(shortCut);
        return menu;
    }

    public static Element createElement(Element superNode) {
        Element node = new Element(StringUtil.EMPTY, superNode);
        EditorContext.refresh();
        return node;
    }


    public static Element createElement(Element superNode, String name, LayoutType layoutType) {
        name = name == null ? StringUtil.EMPTY : name;
        Element node = new Element(name, superNode);
        setElementBounds(node);
        return node;
    }

    public static Shape makeShape(Element element, float x, float y) {
        Rectangle bounds = element.getBounds();
        final float round = 2.0f;
        return new RoundRectangle2D.Double(bounds.x + x, bounds.y + y, bounds.getWidth(), bounds.getHeight(), round, round);
    }

    public static Shape makeShape(Element element) {
        Rectangle bounds = element.getBounds();
        final float round = 2.0f;
        return new RoundRectangle2D.Double(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight(), round, round);
    }

    public static void calcPrepareLine(Element source, Element target) {
        int index = 1;
    }

    public static Rectangle calcTopLeftPoint(Point point, Rectangle bounds) {
        int newX = point.x - bounds.width / 2;
        int newY = point.y - bounds.height / 2;
        return new Rectangle(newX, newY, bounds.width, bounds.height);
    }

    public static void getNodeArea(Element node, Dimension area) {
        int levelWidth = node.getSelfWidth();
        int levelHeight = node.getSelfHeight();
        for (Element child : node.children) {
            if (child.getSelfWidth() > levelWidth) {
                levelWidth = child.getSelfWidth();
            }
            if (child.getSelfHeight() > levelHeight) {
                levelHeight = child.getSelfHeight();
            }
            getNodeArea(child, area);
        }
        if (node.children.size() == 0) {
            return;
        }
        area.width += levelWidth;
        area.height += levelHeight;
    }

    public static void getNodeWidth(Element node, Dimension area) {
        if (node.children.size() == 0) {
            return;
        }
        int levelWidth = node.getBounds().width;
        for (Element child : node.children) {
            if (child.getBounds().width > levelWidth) {
                levelWidth = child.getBounds().width;
            }
            getNodeWidth(child, area);
        }
        if(levelWidth < node.parent.getBounds().width){
            return;
        }
        area.width += levelWidth + 100;
    }

    public static void getNodeHeight(Element node, Dimension area) {
        int levelHeight = node.getSelfHeight();
        for (Element child : node.children) {
            area.height += levelHeight + MindMapConfig.VERTICAL_OFFSET;
            getNodeHeight(child, area);
        }
    }

    public static void setNodeOffset(int x1, int x, int y, Element node) {
        node.addOffset(x1, x, y);
        for (Element child : node.children) {
            setNodeOffset(x1, x, y, child);
        }
    }

    public static void main(String[] args) {
        Element parent = new Element("", null);
        Element c1 = new Element("c1", parent);
        c1.setBounds(0, 0, 100, 50);
        Element c2 = new Element("c2", c1);
        new Element("c2", c1);
        Dimension dimension = new Dimension();
        getNodeArea(parent, dimension);
        System.out.println("dimension = " + dimension);
    }

    /**
     * @param p1 pont 1
     * @param p2 pont 2
     * @return direction of two point
     * <p>
     * 7   0   1
     * 6       2
     * 5   4   3
     */
    public static int getDirection(Rectangle p1, Rectangle p2) {
        double angle = Math.atan((p2.getCenterY() - p2.getCenterY()) / -(p2.getCenterX() - p1.getCenterX()));
        double angle22d5 = Math.PI / 8;
        double angle67d5 = Math.PI / 2 - angle22d5;
        if (angle > -angle22d5 && angle <= angle22d5) {
            if (p1.getCenterX() < p2.getCenterX()) {
                return 2;
            } else {
                return 6;
            }
        } else if (angle > angle22d5 && angle <= angle67d5) {
            if (p1.getCenterX() < p2.getCenterX()) {
                return 1;
            } else {
                return 5;
            }
        } else if (angle > -angle67d5 && angle <= -angle22d5) {
            if (p1.getCenterX() < p2.getCenterX()) {
                return 3;
            } else {
                return 7;
            }
        } else {
            if (p2.getCenterY() < p2.getCenterY()) {
                return 4;
            } else {
                return 0;
            }
        }
    }

    /**
     * @param p1 pont 1
     * @param p2 pont 2
     * @return direction of two point
     * <p>
     * 7   0   1
     * 6       2
     * 5   4   3
     */
    public static Point getDirectionPoint(Rectangle p1, Rectangle p2) {
        int direction = getDirection(p1, p2);
        switch (direction) {
            case 0:
                return new Point((int) p1.getCenterX(), p1.y);
            case 1:
                return new Point(p1.x + p1.width, p1.y);
            case 2:
                return new Point((int) (p1.getCenterX() + p1.width / 2), (int) p1.getCenterY());
            case 3:
                return new Point(p1.x + p1.width, p1.y + p1.height);
            case 4:
                return new Point((int) p1.getCenterX(), p1.y + p1.height);
            case 5:
                return new Point(p1.x, p1.y + p1.height);
            case 6:
                return new Point(p1.x, (int) p1.getCenterY());
            case 7:
                return new Point(p1.x, p1.y);
            default:
                return new Point((int) p1.getCenterX(), (int) p1.getCenterY());
        }
    }

    public static void createNodes(Element node, Table table) {
        Element element = createElement(node, table.getName(), node.layoutType);
        element.type = NodeType.TABLE_NODE;
    }

    public static void createNodes(Element node, String name) {
        Element element = createElement(node, name, node.layoutType);
        element.type = NodeType.ENTITY_NODE;
    }

    public static void createNodes(Element node, List<String> methods) {
        for (String name : methods) {
            Element element = createElement(node, name, node.layoutType);
            element.type = NodeType.PROTOCOL_NODE;
        }
    }
}
        

