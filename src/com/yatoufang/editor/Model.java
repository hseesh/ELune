package com.yatoufang.editor;

import com.yatoufang.editor.component.Canvas;
import com.yatoufang.editor.model.NodeData;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.component.BaseNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.component.LinkLine;
import com.yatoufang.editor.component.impl.*;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.editor.component.UndoRedo;
import com.yatoufang.editor.component.UndoRedoNode;
import com.yatoufang.editor.type.Position;
import com.yatoufang.editor.type.SourceType;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Protocol;
import com.yatoufang.entity.TcpMethod;
import com.yatoufang.service.NotifyService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.FileWrite;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author hse
 * @since 2022/9/9 0009
 */
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;

    private String basePath;

    private String moduleName;
    private transient Canvas canvas;
    private transient Stack<ArrayList<UndoRedoNode>> undoable = new Stack<>();
    private transient Stack<ArrayList<UndoRedoNode>> redoable = new Stack<>();
    private transient String filePath = null;
    private final Set<AbstractNode> nodeList = new LinkedHashSet<>();
    private final Set<AbstractNode> selectedNodes = new HashSet<>();
    private final Set<LinkLine> lines = new HashSet<>();
    private final Connector[] connectors = new Connector[2];
    private boolean connectorsVisible = true;
    private boolean gridVisible = true;
    private boolean dragging = false;

    public Model(Canvas canvas) {
        this.canvas = canvas;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean getDragging() {
        return dragging;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Connector getStartConnector() {
        return connectors[0];
    }

    public void setDrawingSurface(Canvas canvas) {
        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Set<LinkLine> getLines() {
        return lines;
    }

    public boolean isGridVisible() {
        return gridVisible;
    }

    public Set<AbstractNode> getSelectedNodes() {
        return selectedNodes;
    }

    public boolean isSingleSelected() {
        return selectedNodes.size() == 1;
    }

    public boolean isConnectorsVisible() {
        return connectorsVisible;
    }

    public boolean shouldClearLinkLine() {
        return connectors[0] != null;
    }

    public void addTransientComponents() {
        undoable = new Stack<>();
        redoable = new Stack<>();
        for (AbstractNode node : nodeList) {
            node.addTransientComponents();
        }
        for (LinkLine line : lines) {
            line.addTransientComponents();
        }
    }

    public void addListeners() {
        for (AbstractNode node : nodeList) {
            node.addListeners();
        }
    }

    public void undo() {
        if (!undoable.isEmpty()) {
            deSelectAll();
            ArrayList<UndoRedoNode> prevState = undoable.pop();
            ArrayList<UndoRedoNode> currentState = new ArrayList<>(prevState.size());
            for (UndoRedoNode node : prevState) {
                currentState.add(new UndoRedoNode(node.getItem(), new Rectangle(node.getItem().getBounds()), node.getAndToggleEvents()));
            }
            for (UndoRedoNode node : prevState) {
                node.getItem().undo(node);
            }
            redoable.push(currentState);
            update();
        }
    }

    public void redo() {
        if (!redoable.isEmpty()) {
            deSelectAll();
            ArrayList<UndoRedoNode> prevState = redoable.pop();
            ArrayList<UndoRedoNode> currentState = new ArrayList<>(prevState.size());
            for (UndoRedoNode node : prevState) {
                currentState.add(new UndoRedoNode(node.getItem(), new Rectangle(node.getItem().getBounds()), node.getAndToggleEvents()));
            }
            for (UndoRedoNode node : prevState) {
                node.getItem().redo(node);
            }
            undoable.push(currentState);
            update();
        }
    }

    public void addUndoableStackFrame(Set<? extends UndoRedo> items, int... indexes) {
        ArrayList<UndoRedoNode> list = new ArrayList<>();
        for (UndoRedo item : items) {
            list.add(new UndoRedoNode(item, new Rectangle(item.getBounds()), indexes));
        }
        undoable.push(list);
        redoable.clear();
    }

    public void add(AbstractNode node) {
        add(node, true);
    }

    public void add(AbstractNode node, boolean undoable) {
        nodeList.add(node);
        updateAfterAdded(node);

        if (undoable) {
            addUndoableStackFrame(new HashSet<>() {{
                add(node);
            }}, UndoRedoNode.ADDED);
        }
    }

    public void delete(AbstractNode node) {
        delete(node, true);
    }

    public void delete(AbstractNode node, boolean undoable) {
        setSelected(node, true);
        deleteSelected(undoable);
    }

    public void deleteSelected() {
        deleteSelected(true);
    }

    private void deleteSelected(boolean undoable) {
        if (undoable) {
            Set<LinkLine> linesToDelete = new HashSet<>();
            for (AbstractNode node : selectedNodes) {
                linesToDelete.addAll(node.getLines());
            }
            addUndoableStackFrame(new LinkedHashSet<>() {{
                addAll(selectedNodes);
                addAll(linesToDelete);
            }}, UndoRedoNode.DELETED);
        }
        Set<AbstractNode> copySelectedObjects = new HashSet<>(selectedNodes);
        for (AbstractNode node : copySelectedObjects) {
            cleanUpConnections(node);
            nodeList.remove(node);
            selectedNodes.remove(node);
            node.removeInternalComponents();
            canvas.remove(node);
        }

        canvas.requestFocus();
    }

    private void cleanUpConnections(AbstractNode node) {
        Set<LinkLine> copyLines = new HashSet<>(node.getLines());
        for (LinkLine chartLine : copyLines) {
            deleteLine(chartLine, false);
        }
        if (connectors[0] != null && connectors[0].isOwnedBy(node)) {
            clearMakeLine();
        }
    }

    public void clear() {
        nodeList.clear();
        lines.clear();
        filePath = null;
        canvas.removeAll();
        clearMakeLine();
        update();
    }

    private void updateAfterAdded(AbstractNode node) {
        canvas.add(node, 0);
        node.addInternalComponents();

        update();
    }

    public void updateAfterOpened() {
        canvas.removeAll();
        for (AbstractNode node : nodeList) {
            canvas.add(node);
            node.addInternalComponents();
        }
        update();
    }

    public void update() {
        updateLines();
        canvas.revalidate();
        canvas.repaint();
    }

    public void updateLines() {
        for (LinkLine line : lines) {
            line.update();
        }
    }

    public void toggleConnectors() {
        connectorsVisible = !connectorsVisible;
        for (AbstractNode node : nodeList) {
            node.toggleConnectors(connectorsVisible);
        }
    }

    public void toggleGrid() {
        gridVisible = !gridVisible;
        canvas.repaint();
    }

    public void clearMakeLine() {
        connectors[0] = null;
        connectors[1] = null;
    }

    public void selectBoxes(Rectangle selection) {
        for (AbstractNode node : nodeList) {
            setSelected(node, node.isBeingSelected(selection));
        }
    }

    public void setSelected(AbstractNode node, boolean flag) {
        if (flag) {
            selectedNodes.add(node);
            node.setMouseEntered(true);
        } else {
            selectedNodes.remove(node);
            node.setMouseEntered(false);
        }
        canvas.repaint();
    }

    public boolean isSelected(AbstractNode node) {
        return selectedNodes.contains(node);
    }

    public void deSelectAll() {
        for (AbstractNode node : selectedNodes) {
            setSelected(node, false);
        }
        selectedNodes.clear();
    }

    public void selectAll() {
        for (AbstractNode node : nodeList) {
            setSelected(node, true);
        }
    }

    public void moveSelected(int dx, int dy) {
        for (AbstractNode node : selectedNodes) {
            node.dragMove(dx, dy);
        }
        updateLines();
        canvas.repaint();
    }

    public int[] calcImageArea() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = 0, maxY = 0;
        final int padding = 70;
        if (nodeList.isEmpty()) {
            return new int[]{0, 0, GlobalConstant.WIDTH, GlobalConstant.HEIGHT};
        }
        for (AbstractNode node : nodeList) {
            if (node.getX() < minX) {
                minX = node.getX();
                minX = Math.max(minX - padding, 0);
            }
            if (node.getY() < minY) {
                minY = node.getY();
                minY = Math.max(minY - padding, 0);
            }
            if (node.getX() + node.getWidth() > maxX) {
                maxX = node.getX() + node.getWidth();
                maxX = Math.min(maxX + padding, GlobalConstant.WIDTH);
            }
            if (node.getY() + node.getHeight() > maxY) {
                maxY = node.getY() + node.getHeight();
                maxY = Math.min(maxY + padding, GlobalConstant.HEIGHT);
            }
        }
        return new int[]{minX, minY, maxX, maxY};
    }

    public void manageConnection(Connector connector) {
        if (checkSourceType(connector)) {
            return;
        }
        if (connectors[0] == null) {
            connectors[0] = connector;
        } else if (connectors[1] == null) {
            if (!connectors[0].equals(connector)) {
                connectors[1] = connector;
                boolean lineAlreadyExists = false;
                for (LinkLine chartLine : lines) {
                    if (chartLine.hasPairOfConnectors(connectors)) {
                        lineAlreadyExists = true;
                    }
                }
                if (!lineAlreadyExists) {
                    LinkLine line = new LinkLine(this, connectors[0], connectors[1]);
                    addLine(line);
                }
                clearMakeLine();
                canvas.repaint();
            }
        }
    }

    public void addLine(LinkLine line) {
        addLine(line, true);
    }

    public void addLine(LinkLine line, boolean undoable) {
        if (undoable) {
            addUndoableStackFrame(new HashSet<>() {{
                add(line);
            }}, UndoRedoNode.ADDED);
        }
        lines.add(line);
        line.getStartAbstractNode().addLine(line);
        line.getEndAbstractNode().addLine(line);
    }

    public void deleteLine(LinkLine line) {
        deleteLine(line, true);
    }

    public void deleteLine(LinkLine line, boolean undoable) {
        if (undoable) {
            line.getModel().addUndoableStackFrame(new HashSet<>() {{
                add(line);
            }}, UndoRedoNode.DELETED);
        }
        lines.remove(line);
        line.getStartAbstractNode().deleteLine(line);
        line.getEndAbstractNode().deleteLine(line);
    }

    public void render(Graphics2D g2) {
        g2.setColor(ColorBox.LINE.getColor());
        for (LinkLine chartLine : lines) {
            chartLine.drawLine(g2);
        }
        g2.setStroke(GlobalConstant.STROKE_ONE);
    }

    public boolean checkSourceType(Connector connector) {
        if (connectors[0] != null) {
            if (connectors[0].getSourceType() == connector.getSourceType()) {
                connector.setShowErrorFlag(true);
                return true;
            }
        }
        connector.setShowErrorFlag(false);
        connector.setMouseEntered(true);
        return false;
    }

    public void tryCreate(Point point) {
        if (connectors[0] == null) {
            return;
        }
        AbstractNode node = connectors[0].getAbstractNode();
        if (node.getNodeData().getNodeType() != NodeType.PROTOCOL_NODE) {
            clearMakeLine();
            return;
        }
        String name = node.getNodeData().getName();
        String alias = node.getNodeData().getExtra();
        SourceType sourceType = connectors[0].getSourceType();
        switch (sourceType) {
            case IN_PUT:
                RequestNode requestNode = new RequestNode(this, point);
                manageConnection(requestNode.getOutPutConnector());
                requestNode.sync(name, alias);
                add(requestNode);
                break;
            case OUT_PUT:
                Position position = connectors[0].getPosition();
                if (position == Position.ESE) {
                    ResponseNode responseNode = new ResponseNode(this, point);
                    manageConnection(responseNode.getInPutConnector());
                    responseNode.sync(name, alias);
                    add(responseNode);
                } else {
                    PushNode pushNode = new PushNode(this, point);
                    manageConnection(pushNode.getInPutConnector());
                    pushNode.sync(name, alias);
                    add(pushNode);
                }
                break;
            default:
                break;
        }
        clearMakeLine();
    }

    public void createNode(Point point, NodeType nodeType, String content) {
        BaseNode baseNode = new BaseNode(this, point, nodeType);
        baseNode.getNodeData().refresh(content);
        add(baseNode);
    }

    public boolean contains(String name) {
        return nodeList.stream().anyMatch(e -> e.getNodeData().getName().equals(name));
    }

    public Optional<AbstractNode> getNode(String name, NodeType type) {
        return nodeList.stream().filter(k -> k.getNodeData().getName().equals(name) && k.getNodeData().getNodeType() == type).findFirst();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void refreshWorkingSpace(String space) {
        for (AbstractNode abstractNode : selectedNodes) {
            abstractNode.refreshWorkingSpace(space);
        }
    }

    public void onExecute() {
        if (moduleName == null || moduleName.length() == 0) {
            NotifyService.notifyWarning(NotifyKeys.NO_MODULE_SELECTED);
            return;
        }
        ArrayList<AbstractNode> protocols = Lists.newArrayList();
        for (AbstractNode node : nodeList) {
            if (node.getNodeData().getNodeType() == NodeType.PROTOCOL_NODE) {
                protocols.add(node);
                continue;
            }
            if (NodeType.AUTO_WRITE.contains(node.getNodeData().getNodeType())) {
                FileWrite.write(node.getNodeData().getContent(), node.getWorkPath(), true, false);
            }
        }
        int cmdIndex = 0;
        ArrayList<TcpMethod> methods = Lists.newArrayList();
        Param param = new Param(ProjectKeys.ACTOR_ID,ProjectKeys.ACTOR_ID,long.class.getSimpleName());
        for (AbstractNode protocol : protocols) {
            NodeData nodeData = protocol.getNodeData();
            TcpMethod method = new TcpMethod(nodeData.getName(), nodeData.getName());
            for (AbstractNode node : protocol.getAllLinkNode()) {
                switch (node.getNodeData().getNodeType()){
                    case RUSH_NODE:
                        method.setPush(node.getNodeData().getAlias());
                        break;
                    case REQUEST_NODE:
                        method.setRequest(node.getNodeData().getAlias());
                        break;
                    case RESPONSE_NODE:
                        method.setResponse(node.getNodeData().getAlias());
                        break;
                    default:break;
                }
            }
            method.add(param);
            methods.add(method);
            method.setAlias(nodeData.getAlias());
            method.setCmdCode(String.valueOf(++cmdIndex));
            method.addAll(nodeData.getMetaData().getPramList());
        }
        Protocol protocol = Protocol.valueOf(moduleName, moduleName, moduleName, methods);
        VelocityService velocityService = VelocityService.getInstance();
        String execute = velocityService.execute(ProjectKeys.CMD_TEMPLATE, protocol);
        System.out.println(execute);
    }
}