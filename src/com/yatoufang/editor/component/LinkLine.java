package com.yatoufang.editor.component;

import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.editor.Model;
import com.yatoufang.editor.menus.LinkLineMenu;
import com.yatoufang.editor.type.SourceType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.io.Serializable;


/**
 * @author hse
 * @since 2022/9/9 0009
 */
public class LinkLine implements Serializable, UndoRedo {

    private static final long serialVersionUID = 1L;
    private Model model;
    private Connector endConnector;
    private Connector startConnector;
    private transient LinkLineMenu contextMenu;
    private final Path2D path = new Path2D.Double();
    private final Rectangle undoHelp = new Rectangle();

    public LinkLine(Model model, Connector startConnector, Connector endConnector) {
        this.model = model;
        this.startConnector = startConnector;
        this.endConnector = endConnector;
        addTransientComponents();
        update();
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
        return undoHelp;
    }

    private void undoRedoHelp(UndoRedoNode node) {
        if (node.isSet(UndoRedoNode.ADDED)) {
            model.deleteLine(this, false);
        } else if (node.isSet(UndoRedoNode.DELETED)) {
            model.addLine(this, false);
        }
    }

    public LinkLine copy() {
        return new LinkLine(model, startConnector, endConnector);
    }

    public void addTransientComponents() {
        contextMenu = new LinkLineMenu(this);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }


    public Connector getStartConnector() {
        return startConnector;
    }

    public Connector getEndConnector() {
        return endConnector;
    }

    public AbstractNode getStartAbstractNode() {
        return startConnector.getAbstractNode();
    }

    public AbstractNode getEndAbstractNode() {
        return endConnector.getAbstractNode();
    }


    public void showContextMenu(MouseEvent e) {
        double zoomFactor = model.getCanvas().getRootPanel().getZoomPanel().getScale();
        int x = (int) (e.getX() * zoomFactor);
        int y = (int) (e.getY() * zoomFactor);
        contextMenu.show(e.getComponent(), x, y);
    }

    public void update() {
        path.reset();
        Connector end = this.endConnector;
        Connector start = this.startConnector;
        path.moveTo(start.getCenterX(), start.getCenterY());
        path.curveTo(start.getCenterX(), start.getCenterY(), start.getCenterX(), end.getCenterY(), end.getCenterX(), end.getCenterY());
    }

    public void setStartConnector(Connector connector) {
        startConnector = connector;
    }

    public void setEndConnector(Connector connector) {
        endConnector = connector;
    }


    public boolean hasPairOfConnectors(Connector[] makeLine) {
        return (startConnector.equals(makeLine[0]) && endConnector.equals(makeLine[1])) || (startConnector.equals(makeLine[1]) && endConnector.equals(makeLine[0]));
    }

    public void drawLine(Graphics2D g2) {
        AbstractNode inPutNode = getInPutNode();
        g2.setColor(inPutNode.getLineColor());
        if (inPutNode.getNodeData().getNodeType() == NodeType.RUSH_NODE) {
            g2.setStroke(GlobalConstant.STROKE_TWO_DOTTED);
        } else {
            g2.setStroke(GlobalConstant.STROKE_TWO);
        }
        g2.draw(path);
    }

    public Path2D getPath() {
        return path;
    }


    public AbstractNode getInPutNode() {
        if (startConnector.getSourceType() == SourceType.IN_PUT) {
            return startConnector.getAbstractNode();
        }
        return endConnector.getAbstractNode();
    }


    public AbstractNode getOutPutNode() {
        if (startConnector.getSourceType() == SourceType.OUT_PUT) {
            return startConnector.getAbstractNode();
        }
        return endConnector.getAbstractNode();
    }

}