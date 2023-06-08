package com.yatoufang.editor.component;

import com.yatoufang.editor.Model;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.editor.listeners.NodeKeyListener;
import com.yatoufang.editor.listeners.NodeMouseListener;
import com.yatoufang.editor.listeners.NodeMouseMotionListener;
import com.yatoufang.editor.menus.NodeMenu;
import com.yatoufang.editor.model.NodeData;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.type.Position;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * @author hse
 * @since 2022/9/4 0004
 */
public abstract class AbstractNode extends JPanel implements UndoRedo, LifeCycleEvent {

    private static final long serialVersionUID = 123456L;
    private final Map<Position, Connector> connectors = new HashMap<>();
    private transient Point startPoint = new Point(0, 0);
    private final Set<LinkLine> linkLines = new HashSet<>();
    protected transient boolean mouseEntered = true;
    private transient boolean resizing = false;
    private transient NodeMenu contextMenu;
    protected NodeData nodeData;
    private Model model;

    public abstract AbstractNode copy();

    public abstract String getWorkPath();

    public abstract Color getLineColor();

    public abstract void doCommand(KeyEvent e);

    @Override
    public void onCreate() {

    }

    @Override
    public void onInitialize() {

    }

    @Override
    public void onSynchronized(String name, String alias) {

    }

    @Override
    public void onReload() {
        contextMenu = new NodeMenu(this);
        this.nodeData.reload();
        this.onInitialize();
        refreshBounds();
    }

    public AbstractNode(NodeData nodeData, Model model) {
        this.nodeData = nodeData;
        this.model = model;
        setFocusable(true);
        onReload();
    }

    public void addConnectors() {
    }

    public void refreshBounds() {
        Rectangle bounds = getBounds();
        int size = nodeData.getMetaData().getPramList().size();
        int width = nodeData.getMetaData().calcOptimumWidth();
        width = Math.max(width, nodeData.getTitleWidth());
        width = Math.max(width, GlobalConstant.NODE_WIDTH);
        if (size > 2) {
            setBounds((int) bounds.getX(), (int) bounds.getY(), width, GlobalConstant.NODE_HEIGHT + (size - 2) * 25);
        } else {
            setBounds((int) bounds.getX(), (int) bounds.getY(), width, (int) bounds.getHeight());
        }
        setConnectorLocation();
    }

    public Collection<Connector> getAllConnectors() {
        return getConnectors().values();
    }


    public void addListeners() {
        addMouseListener(new NodeMouseListener(this));
        addMouseMotionListener(new NodeMouseMotionListener(this));
        addKeyListener(new NodeKeyListener(this));
        for (Connector c : connectors.values()) {
            c.addListeners();
        }
    }

    private void undoRedoHelp(UndoRedoNode node) {
        if (node.isSet(UndoRedoNode.MOVED) || node.isSet(UndoRedoNode.RESIZED)) {
            setBounds(node.getBounds());
            model.setSelected(this, true);
        } else if (node.isSet(UndoRedoNode.ADDED)) {
            model.delete(this, false);
        } else if (node.isSet(UndoRedoNode.DELETED)) {
            model.add(this, false);
            model.setSelected(this, true);
        }
    }

    public void addInternalComponents() {
        for (Connector c : connectors.values()) {
            model.getCanvas().add(c, 0);
        }
    }

    public void removeInternalComponents() {
        for (Connector c : connectors.values()) {
            model.getCanvas().remove(c);
        }
    }

    public void resize() {
        revalidate();
        setConnectorLocation();
        model.updateLines();
        model.getCanvas().repaint();
    }

    public void setConnectorLocation() {
        for (Connector c : connectors.values()) {
            c.setLocation();
        }
    }

    public void toggleConnectors(boolean flag) {
        for (Connector c : connectors.values()) {
            c.setVisible(flag);
        }
    }


    public void refresh(String content) {
        nodeData.refresh(content);
        refreshBounds();
    }

    @Override
    public void undo(UndoRedoNode node) {
        undoRedoHelp(node);
    }

    @Override
    public void redo(UndoRedoNode node) {
        undoRedoHelp(node);
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        setConnectorLocation();
    }

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
        setConnectorLocation();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    protected void addConnector(Connector c) {
        connectors.put(c.getPosition(), c);
    }

    public boolean isResizing() {
        return resizing;
    }

    public void setMouseEntered(boolean mouseEntered) {
        this.mouseEntered = mouseEntered;
        if (mouseEntered) {
            setBorder(new RoundBorder(ColorBox.CONNECTOR_DEFAULT.getColor()));
        } else {
            setBorder(new RoundBorder());
        }
    }

    public void refreshWorkingSpace(String workingSpace) {
        nodeData.refreshWorkingSpace(workingSpace);
    }

    public void setResizing(boolean value) {
        resizing = value;
    }

    public boolean isBeingSelected(Rectangle selection) {
        return selection.intersects(getBounds());
    }

    public JPopupMenu getContextMenu() {
        return contextMenu;
    }

    public Set<LinkLine> getLines() {
        return linkLines;
    }

    public Connector getConnector(Position position) {
        return connectors.get(position);
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void addLine(LinkLine chartLine) {
        linkLines.add(chartLine);
    }

    public void deleteLine(LinkLine chartLine) {
        linkLines.remove(chartLine);
    }

    public void dragMove(int dx, int dy) {
        setLocation(getX() + dx, getY() + dy);
    }

    public NodeData getNodeData() {
        return nodeData;
    }

    public Map<Position, Connector> getConnectors() {
        return connectors;
    }

    public Set<LinkLine> getLinkLines() {
        return linkLines;
    }

    public boolean isMouseEntered() {
        return mouseEntered;
    }

    public void setContextMenu(NodeMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public void setNodeData(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public Model getModel() {
        return model;
    }

    public Collection<AbstractNode> getAllLinkNode() {
        ArrayList<AbstractNode> list = Lists.newArrayList();
        for (LinkLine linkLine : linkLines) {
            AbstractNode outPutNode = linkLine.getOutPutNode();
            if (this.equals(outPutNode)) {
                list.add(linkLine.getInPutNode());
                continue;
            }
            list.add(outPutNode);
        }
        return list;
    }

    public Optional<AbstractNode> getLinkNode(NodeType nodeType) {
        for (LinkLine linkLine : linkLines) {
            AbstractNode outPutNode = linkLine.getOutPutNode();
            if (this.equals(outPutNode)) {
                continue;
            }
            if (nodeType.equals(outPutNode.getNodeData().getNodeType())) {
                return Optional.of(outPutNode);
            }
            AbstractNode inPutNode = linkLine.getInPutNode();
            if (this.equals(outPutNode)) {
                continue;
            }
            if (nodeType.equals(inPutNode.getNodeData().getNodeType())) {
                return Optional.of(inPutNode);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return nodeData.getName();
    }
}