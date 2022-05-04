package com.yatoufang.test.style.event;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.style.AbstractNodeEventParser;
import com.yatoufang.test.style.NodeType;
import com.yatoufang.ui.TableTemplateDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2022/5/1 0001
 */
public class TableEventParser extends AbstractNodeEventParser {
    @Override
    public NodeType getType() {
        return NodeType.TABLE_NODE;
    }

    @Override
    public void preview() {
        onExecute();
    }

    @Override
    public void onExecute() {
        JBPopupFactory instance = JBPopupFactory.getInstance();
        TableTemplateDialog dialog = new TableTemplateDialog("","workSpace");
        instance.createComponentPopupBuilder(dialog.getRootPanel(), dialog.preferableFocusComponent())
                .setTitle("My Table")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(900, 700))
                .addListener(new JBPopupListener() {
                    @Override
                    public void onClosed(@NotNull LightweightWindowEvent event) {
                        JBPopupListener.super.onClosed(event);
                        String content = dialog.getContent();
                        EditorContext.setDesigner(content);
                    }
                })
                .createPopup()
                .showInFocusCenter();
    }

    @Override
    public boolean isSuperNode() {
        return true;
    }
}
