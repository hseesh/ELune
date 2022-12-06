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
        // Clear prev copied objects, iff there is selected objects
        if(!model.getSelectedNodes().isEmpty()) {
            COPY_PASTE.clear();

            // Sets to remember what lines that needs to be copied
            Set<LinkLine> linesToCopyHelp = new HashSet<>();
            Set<LinkLine> linesToCopy = new HashSet<>();
            List<LinkLine> copiedLines = new ArrayList<>();

            // Keeps track of the original AbstractNode and the copy
            // Makes its easier to copy the lines and attach them to the copied objects connector
            Map<AbstractNode, AbstractNode> originalAndCopyPaired = new HashMap<>();

            // Copy all selected objects
            for(AbstractNode co : model.getSelectedNodes()) {
                AbstractNode copy = co.copy();

                // Store original and copied as a mapped pair
                originalAndCopyPaired.put(co, copy);

                // Store the new copy so that is can be pasted later
                COPY_PASTE.add(copy);

                // Find what lines to copy, only lines explicitly between two AbstractNodes that both are copied
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

            // Copy the lines
            for(LinkLine line : linesToCopy) {
                copiedLines.add(line.copy());
            }

            // Add lines to model and Connect the copied line with its copied AbstractNode
            for(LinkLine line : copiedLines) {
                // Prepare values
                AbstractNode ob1 = originalAndCopyPaired.get(line.getStartAbstractNode());
                AbstractNode ob2 = originalAndCopyPaired.get(line.getEndAbstractNode());
                Connector c1 = ob1.getConnector(line.getStartConnector().getPosition());
                Connector c2 = ob2.getConnector(line.getEndConnector().getPosition());

                // Set the copied lines new connectors
                line.setStartConnector(c1);
                line.setEndConnector(c2);

                // Add line to both AbstractNodes
                ob1.addLine(line);
                ob2.addLine(line);
            }
        }
    }

    public static void paste(Model model) {
        model.deSelectAll();

        for(AbstractNode co : COPY_PASTE) {
            // Offset pasted objects
            co.setLocation(co.getX() + 200, co.getY() + 100);

            // Update so that the object has the right model
            // It can be a new instance from which it was copied
            co.setModel(model);

            model.add(co);

            for(LinkLine line : co.getLines()) {
                line.setModel(model);
                model.addLine(line);
            }

            // Select the object so it easily can be moved, after the paste
            // And to that it can be copied again
            model.setSelected(co, true);
        }

        // Update all lines to make sure they are correctly drawn
        model.updateLines();

        // Copy objects that was pasted, enabling multiple paste
        CopyPasteHelper.copy(model);
    }
}