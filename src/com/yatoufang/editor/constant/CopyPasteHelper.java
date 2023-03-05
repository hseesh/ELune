package com.yatoufang.editor.constant;


import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.component.Connector;
import com.yatoufang.editor.component.LinkLine;
import com.yatoufang.editor.Model;

import java.util.*;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public final class CopyPasteHelper {
    private CopyPasteHelper() {}
    private static final Set<AbstractNode> COPY_PASTE = new HashSet<>();

    public static void copy(Model model) {
        if(!model.getSelectedNodes().isEmpty()) {
            COPY_PASTE.clear();
            Set<LinkLine> linesToCopyHelp = new HashSet<>();
            Set<LinkLine> linesToCopy = new HashSet<>();
            List<LinkLine> copiedLines = new ArrayList<>();
            Map<AbstractNode, AbstractNode> originalAndCopyPaired = new HashMap<>();
            for(AbstractNode co : model.getSelectedNodes()) {
                AbstractNode copy = co.copy();
                originalAndCopyPaired.put(co, copy);
                COPY_PASTE.add(copy);
                for(LinkLine line : co.getLines()) {
                    if(line.getStartAbstractNode().equals(line.getEndAbstractNode())) {
                        linesToCopy.add(line);
                    }

                    if(linesToCopyHelp.contains(line)) {
                        linesToCopy.add(line);
                        linesToCopyHelp.remove(line);
                    }else {
                        linesToCopyHelp.add(line);
                    }
                }
            }
            for(LinkLine line : linesToCopy) {
                copiedLines.add(line.copy());
            }
            for(LinkLine line : copiedLines) {
                AbstractNode ob1 = originalAndCopyPaired.get(line.getStartAbstractNode());
                AbstractNode ob2 = originalAndCopyPaired.get(line.getEndAbstractNode());
                Connector c1 = ob1.getConnector(line.getStartConnector().getPosition());
                Connector c2 = ob2.getConnector(line.getEndConnector().getPosition());
                line.setStartConnector(c1);
                line.setEndConnector(c2);
                ob1.addLine(line);
                ob2.addLine(line);
            }
        }
    }

    public static void paste(Model model) {
        model.deSelectAll();
        for(AbstractNode co : COPY_PASTE) {
            co.setLocation(co.getX() + 200, co.getY() + 100);
            co.setModel(model);
            model.add(co);
            for(LinkLine line : co.getLines()) {
                line.setModel(model);
                model.addLine(line);
            }
            model.setSelected(co, true);
        }
        model.updateLines();
        CopyPasteHelper.copy(model);
    }
}