package com.yatoufang.editor;

import com.google.common.collect.Maps;
import com.yatoufang.editor.component.*;
import com.yatoufang.editor.component.impl.DataBaseNode;
import com.yatoufang.editor.component.impl.PushNode;
import com.yatoufang.editor.component.impl.RequestNode;
import com.yatoufang.editor.component.impl.ResponseNode;
import com.yatoufang.editor.constant.ColorBox;
import com.yatoufang.editor.constant.GlobalConstant;
import com.yatoufang.editor.model.NodeData;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.editor.type.Position;
import com.yatoufang.editor.type.SourceType;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Protocol;
import com.yatoufang.entity.TcpMethod;
import com.yatoufang.service.NotifyService;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Expression;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.dialog.edit.ProtocolPreviewDialog;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author hse
 * @since 2022/9/9 0009
 */
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    private String basePath;
    private String moduleName;
    private transient RootCanvas rootCanvas;
    private transient Stack<ArrayList<UndoRedoNode>> undoable;
    private transient Stack<ArrayList<UndoRedoNode>> redouble;
    private transient String filePath = null;
    private final Set<AbstractNode> nodeList = new LinkedHashSet<>();
    private final Set<AbstractNode> selectedNodes = new CopyOnWriteArraySet<>();
    private final Set<LinkLine> lines = new HashSet<>();
    private final Connector[] connectors = new Connector[2];
    private boolean connectorsVisible = true;
    private boolean gridVisible = true;
    private boolean dragging = false;

    public Model(RootCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    public Model() {
        undoable = new Stack<>();
        redouble = new Stack<>();
    }

    public RootCanvas getRootCanvas() {
        return rootCanvas;
    }

    public void setRootCanvas(RootCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
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

    public RootCanvas getCanvas() {
        return rootCanvas;
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

    public void addComponents() {
        undoable = new Stack<>();
        redouble = new Stack<>();
        for (AbstractNode node : nodeList) {
            node.onReload();
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
            redouble.push(currentState);
            update();
        }
    }

    public void redo() {
        if (!redouble.isEmpty()) {
            deSelectAll();
            ArrayList<UndoRedoNode> prevState = redouble.pop();
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
        redouble.clear();
    }

    public void add(AbstractNode node) {
        if (node.getNodeData().getNodeType() == NodeType.DATA_BASE) {
            if (getNodesByType(NodeType.DATA_BASE).size() > 0) {
                return;
            }
        }
        if (nodeList.contains(node)) {
            return;
        }
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
            nodeList.removeIf(e -> Objects.equals(e.getNodeData().getName(), node.getNodeData().getName()) && node.getNodeData().getNodeType() == e.getNodeData().getNodeType());
            selectedNodes.remove(node);
            node.removeInternalComponents();
            rootCanvas.remove(node);
        }

        rootCanvas.requestFocus();
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
        clearMakeLine();
        rootCanvas.removeAll();
        update();
    }

    private void updateAfterAdded(AbstractNode node) {
        rootCanvas.add(node, 0);
        node.addInternalComponents();
        update();
    }

    public void updateAfterOpened() {
        rootCanvas.removeAll();
        for (AbstractNode node : nodeList) {
            rootCanvas.add(node);
            node.addInternalComponents();
        }
        update();
    }

    public void update() {
        updateLines();
        rootCanvas.revalidate();
        rootCanvas.repaint();
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
        rootCanvas.repaint();
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
        rootCanvas.repaint();
    }

    public boolean isSelected(AbstractNode node) {
        return selectedNodes.contains(node);
    }

    public void deSelectAll() {
        //todo ConcurrentModificationException
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
        rootCanvas.repaint();
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
                rootCanvas.repaint();
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
                requestNode.onSynchronized(name, alias);
                add(requestNode);
                break;
            case OUT_PUT:
                Position position = connectors[0].getPosition();
                if (position == Position.ESE) {
                    ResponseNode responseNode = new ResponseNode(this, point);
                    manageConnection(responseNode.getInPutConnector());
                    responseNode.onSynchronized(name, alias);
                    add(responseNode);
                } else {
                    PushNode pushNode = new PushNode(this, point);
                    manageConnection(pushNode.getInPutConnector());
                    pushNode.onSynchronized(name, alias);
                    add(pushNode);
                }
                break;
            default:
                break;
        }
        clearMakeLine();
    }


    public Set<AbstractNode> getNodeList() {
        return nodeList;
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


    public List<AbstractNode> getNodesByType(NodeType nodeType) {
        return nodeList.stream().filter(e -> e.getNodeData().getNodeType() == nodeType).collect(Collectors.toList());
    }

    public List<AbstractNode> getNodesByType(Collection<NodeType> nodeTypes) {
        return nodeList.stream().filter(e -> nodeTypes.contains(e.getNodeData().getNodeType())).collect(Collectors.toList());
    }

    public Optional<AbstractNode> getNodeByName(String alias) {
        return nodeList.stream().filter(e -> e.getNodeData().getAlias().equals(alias)).findFirst();
    }


    public void onExecute(boolean preview, boolean execute) {
        if (moduleName == null || moduleName.length() == 0) {
            NotifyService.notifyWarning(NotifyKeys.NO_MODULE_SELECTED);
            return;
        }
        HashMap<String, String> map = Maps.newHashMap();
        ArrayList<AbstractNode> protocols = Lists.newArrayList();
        ArrayList<AbstractNode> returnNodes = Lists.newArrayList();
        for (AbstractNode node : nodeList) {
            if (node.getNodeData().getNodeType() == NodeType.PROTOCOL_NODE) {
                protocols.add(node);
                continue;
            }
            if (node.getNodeData().getNodeType() == NodeType.DATA_BASE) {
                DataBaseNode dataBaseNode = (DataBaseNode) node;
                map.putAll(dataBaseNode.getContent());
                continue;
            }
            if (NodeType.AUTO_WRITE.contains(node.getNodeData().getNodeType())) {
                returnNodes.add(node);
            }
        }
        int cmdIndex = 0, pushIndex = 100;
        ArrayList<String> pushNodes = Lists.newArrayList();
        ArrayList<TcpMethod> methods = Lists.newArrayList();
        List<AbstractNode> entities = getNodesByType(List.of(NodeType.ENTITY_NODE, NodeType.DATA_BASE));
        Param param = new Param(ProjectKeys.ACTOR_ID, ProjectKeys.ACTOR_ID, long.class.getSimpleName());
        for (AbstractNode abstractNode : protocols) {
            NodeData nodeData = abstractNode.getNodeData();
            TcpMethod method = new TcpMethod(nodeData.getExtra(), nodeData.getName());
            for (AbstractNode node : abstractNode.getAllLinkNode()) {
                String alias = node.getNodeData().getAlias();
                switch (node.getNodeData().getNodeType()) {
                    case RUSH_NODE:
                        if (!pushNodes.contains(alias)) {
                            pushNodes.add(alias);
                            method.setPushCode(String.valueOf(pushIndex++));
                        }
                        method.setPush(alias);
                        break;
                    case REQUEST_NODE:
                        method.setRequest(alias);
                        method.addAll(node.getNodeData().getMetaData().getPramList());
                        break;
                    case RESPONSE_NODE:
                        method.setResponse(alias);
                        break;
                    default:
                        break;
                }
            }
            method.add(param);
            methods.add(method);
            method.setAlias(nodeData.getAlias());
            method.setCmdCode(String.valueOf(++cmdIndex));
            calcMethodReturnType(method, abstractNode, entities);
        }
        Protocol protocol = Protocol.valueOf(moduleName, StringUtil.getUpperCaseVariable(moduleName), moduleName, methods);
        VelocityService velocityService = VelocityService.getInstance();

        String cmd = velocityService.execute(ProjectKeys.CMD_TEMPLATE, protocol);
        String facade = velocityService.execute(ProjectKeys.FACADE_TEMPLATE, protocol);
        String handler = velocityService.execute(ProjectKeys.HANDLER_TEMPLATE, protocol);
        String facadeImpl = velocityService.execute(ProjectKeys.FACADE_IMPL_TEMPLATE, protocol);

        if (preview) {
            map.put(ProjectKeys.CMD, cmd);
            map.put(ProjectKeys.FACADE, facade);
            map.put(ProjectKeys.IMPL, facadeImpl);
            map.put(ProjectKeys.HANDLER, handler);
            new ProtocolPreviewDialog(map, moduleName, basePath, returnNodes).show();
        }
    }

    private void calcMethodReturnType(TcpMethod method, AbstractNode node, List<AbstractNode> entities) {
        method.createValueOf();
        Optional<AbstractNode> optional = node.getLinkNode(NodeType.RESPONSE_NODE);
        if (optional.isEmpty()) {
            return;
        }
        node = optional.get();
        if (node.getNodeData().getMetaData().getPramList().size() == 1) {
            Param next = node.getNodeData().getMetaData().getPramList().iterator().next();
            String returnType = String.format(Expression.RESULT_OF, next.getTypeAlias());
            method.setReturnType(returnType);
            return;
        }
        Collection<Param> pramList = node.getNodeData().getMetaData().getPramList();
        if (pramList.isEmpty()) {
            return;
        }
        Map<String, Integer> countMap = Maps.newHashMap();
        for (AbstractNode entity : entities) {
            String alias = entity.getNodeData().getAlias();
            Collection<Param> params = entity.getNodeData().getMetaData().getPramList();
            for (Param param : params) {
                for (Param customary : pramList) {
                    if (customary.getName().equals(param.getName())) {
                        countMap.put(alias, countMap.getOrDefault(alias, 0) + 1);
                    }
                }
            }
        }
        int counter = 0;
        String key = ProjectKeys.RESULT_OF;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > counter) {
                key = entry.getKey();
                key = String.format(Expression.RESULT_OF, key);
            }
        }
        method.setReturnType(key);
    }

}