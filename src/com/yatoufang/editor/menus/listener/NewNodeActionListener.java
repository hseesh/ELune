package com.yatoufang.editor.menus.listener;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.templet.Expression;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.dialog.ConfigTemplateDialog;
import com.yatoufang.ui.dialog.TableTemplateDialog;
import com.yatoufang.ui.dialog.edit.EntityBuildDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author hse
 * @since 2022/11/10 0010
 */
public class NewNodeActionListener implements ActionListener {

    public final AbstractNode node;

    public NewNodeActionListener(AbstractNode node) {
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String moduleName = node.getModel().getModuleName();
        String content = node.getNodeData().getContent();
        String workPath = node.getModel().getBasePath().replaceAll(Expression.FORMAT_FLAG, ProjectKeys.GAME_SERVER);
        switch (node.getNodeData().getNodeType()) {
            case ENTITY_NODE:
            case RUSH_NODE:
            case REQUEST_NODE:
            case RESPONSE_NODE:
            case ENUM_CONFIG:
                new EntityBuildDialog(node).show();
                break;
            case NORMAL_CONFIG:
                new ConfigTemplateDialog(workPath, node).show();
                break;
            case DATA_BASE:
                JBPopupFactory instance = JBPopupFactory.getInstance();
                TableTemplateDialog dialog = new TableTemplateDialog(workPath, moduleName, content);
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
                                node.refresh(content);
                            }
                        })
                        .createPopup()
                        .showInFocusCenter();
                break;
            default:
                break;
        }

    }

}
