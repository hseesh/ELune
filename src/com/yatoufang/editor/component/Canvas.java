package com.yatoufang.editor.component;

import com.intellij.util.ui.UIUtil;
import com.yatoufang.editor.constant.*;
import com.yatoufang.editor.listeners.RootPanelKeyListener;
import com.yatoufang.editor.listeners.RootPanelMouseListener;
import com.yatoufang.editor.listeners.RootPanelMouseMotionListener;
import com.yatoufang.editor.listeners.RootPanelMouseWheelListener;
import com.yatoufang.editor.Model;
import com.yatoufang.editor.menus.RootPanelMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author hse
 * @since 2022/9/6 0006
 */
public class Canvas extends JPanel {
    private static final long serialVersionUID = 1L;

    private Model model;

    private final RootPanel rootPanel;
    private final RootPanelMenu contextMenu;
    private Point clickedPoint = new Point(0, 0);
    private Point cursorPoint = new Point(0, 0);
    private final Rectangle selection = new Rectangle();

    public Canvas(RootPanel rootPanel) {
        this.rootPanel = rootPanel;
        model = new Model(this);
        contextMenu = new RootPanelMenu(this);

        setLayout(null);
        setAutoscrolls(true);
        setPreferredSize(new Dimension(GlobalConstant.WIDTH, GlobalConstant.HEIGHT));
        setBackground(ColorBox.ROOT_PANEL_BANK_GROUND.getColor());

        addMouseListener(new RootPanelMouseListener(this));
        addMouseMotionListener(new RootPanelMouseMotionListener(this));
        addMouseWheelListener(new RootPanelMouseWheelListener(this));
        addKeyListener(new RootPanelKeyListener(this));

    }

    public void processEvent(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            model.redo();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            model.undo();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
            CopyPasteHelper.copy(model);
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
            CopyPasteHelper.paste(model);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            model.clearMakeLine();
            model.update();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
            model.selectAll();
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            FileManager.save(model);
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
            openFile();
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            model.deleteSelected();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            model.moveSelected(0, e.isControlDown() ? -1 : -5);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            model.moveSelected(0, e.isControlDown() ? 1 : 5);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            model.moveSelected(e.isControlDown() ? 1 : 5, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            model.moveSelected(e.isControlDown() ? -1 : -5, 0);
        } else {
            CommandHelper.setCommand(e);
        }
    }

    public Model getModel() {
        return model;
    }

    public JPopupMenu getContextMenu() {
        return contextMenu;
    }

    public void setClickedPoint(Point p) {
        clickedPoint = p;
    }

    public Point getClickedPoint() {
        return clickedPoint;
    }

    public void setCursorPoint(Point p) {
        cursorPoint = p;
    }

    public Rectangle getSelection() {
        return selection;
    }

    public void openFile() {
        Model tempModel = FileManager.loadFromFile();
        if (tempModel != null) {
            model = tempModel;
            model.setDrawingSurface(this);
            model.addTransientComponents();
            model.addListeners();
            model.updateAfterOpened();
        }
    }


    public void exportAsImage() {
        final int minX = 0;
        final int minY = 1;
        final int maxX = 2;
        final int maxY = 3;
        int[] imageArea = model.calcImageArea();
        BufferedImage image = UIUtil.createImage(imageArea[maxX], imageArea[maxY], BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        image = image.getSubimage(imageArea[minX], imageArea[minY], imageArea[maxX] - imageArea[minX], imageArea[maxY] - imageArea[minY]);
        FileManager.saveImage(image);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (model.isGridVisible()) {
            g2.setStroke(new BasicStroke(0.1f));
            g2.setColor(ColorBox.SELECTION_BACKGROUND.getColor());
            final int sideLength = 20;
            int nRowCount = getHeight() / sideLength;
            int currentY = sideLength;
            for (int i = 0; i < nRowCount; i++) {
                g2.drawLine(0, currentY, getWidth(), currentY);
                currentY += sideLength;
            }
            int nColumnCount = getWidth() / sideLength;
            int currentX = sideLength;
            for (int i = 0; i < nColumnCount; i++) {
                g2.drawLine(currentX, 0, currentX, getHeight());
                currentX += sideLength;
            }
        }
        g2.setColor(ColorBox.SELECTION_BACKGROUND.getColor());
        g2.fillRect((int) selection.getX(), (int) selection.getY(), (int) selection.getWidth(), (int) selection.getHeight());
        g2.setColor(ColorBox.SELECTION_BORDER.getColor());
        g2.drawRect((int) selection.getX(), (int) selection.getY(), (int) selection.getWidth(), (int) selection.getHeight());
        Connector connector = model.getStartConnector();
        if (connector != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(ColorBox.LINE.getColor());
            g2.drawLine(connector.getCenterX(), connector.getCenterY(), cursorPoint.x, cursorPoint.y);
        }
        model.render(g2);
    }

    public RootPanel getRootPanel() {
        return rootPanel;
    }

}