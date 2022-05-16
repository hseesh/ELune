package com.yatoufang.test.style.impl.event;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Table;
import com.yatoufang.templet.Annotations;
import com.yatoufang.test.event.EditorContext;
import com.yatoufang.test.model.Element;
import com.yatoufang.test.style.AbstractNodeEventParser;
import com.yatoufang.test.style.NodeType;
import com.yatoufang.ui.dialog.TableTemplateDialog;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.SwingUtils;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String tableContent = EditorContext.designer.getTableContent();
        if(tableContent == null || tableContent.length() == 0){
            return;
        }
        String info = PSIUtil.getFilePrimaryInfo(tableContent,null);
        SwingUtils.createPreviewWindow(info);
    }

    @Override
    public void onPreview(Element node) {

    }

    @Override
    public void onExecute() {
        JBPopupFactory instance = JBPopupFactory.getInstance();
        TableTemplateDialog dialog = new TableTemplateDialog(EditorContext.filePath, "", EditorContext.designer.getTableContent());
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
