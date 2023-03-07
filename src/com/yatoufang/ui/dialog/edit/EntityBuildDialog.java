package com.yatoufang.ui.dialog.edit;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.editor.constant.ContextHelp;
import com.yatoufang.editor.model.MetaData;
import com.yatoufang.editor.type.NodeType;
import com.yatoufang.entity.FileNode;
import com.yatoufang.entity.Param;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.FileWrite;
import com.yatoufang.utils.StringUtil;
import com.yatoufang.utils.SwingUtils;
import icons.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

/**
 * @author hse
 * @since 2022/11/11 0011
 */
public class EntityBuildDialog extends DialogWrapper {

    private EditorTextField editor;
    private AbstractNode classNode;
    private VelocityService velocityService;

    public EntityBuildDialog() {
        super(Application.project, true);
        velocityService = VelocityService.getInstance();
        editor = SwingUtils.createEditor(StringUtil.EMPTY);
        editor.setFont(new Font(null, Font.PLAIN, 14));
        init();
        onEdit();
    }

    public EntityBuildDialog(AbstractNode classNode) {
        super(Application.project, true);
        initData(classNode);
        init();
        if (classNode.getNodeData().getContent().length() == 0) {
            onEdit();
        }
    }

    private void initData(AbstractNode classNode) {
        this.classNode = classNode;
        velocityService = VelocityService.getInstance();
        editor = SwingUtils.createEditor(classNode.getNodeData().getContent());
        editor.setFont(new Font(null, Font.PLAIN, 14));
        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                classNode.getNodeData().setContent(editor.getText());
            }
        });
    }

    private void saveFile() {
        classNode.getNodeData().setContent(editor.getText());
        String workPath = classNode.getModel().getBasePath();
        String moduleName = classNode.getModel().getModuleName();
        if (workPath == null || moduleName == null) {
            return;
        }
        if (NodeType.COULD_WRITE.contains(classNode.getNodeData().getNodeType())) {
            String filePath = StringUtil.buildPath(workPath, ProjectKeys.MODULE, moduleName, ProjectKeys.MODEL, classNode.getNodeData().getMetaData().getName() + ProjectKeys.JAVA);
            FileWrite.write(classNode.getNodeData().getContent(), filePath, true, false);
            doCancelAction();
        }
    }


    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (classNode == null) {
            return;
        }
        classNode.refresh(editor.getText());
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMinimumSize(new Dimension(1100, 800));
        panel.add(editor);
        return panel;
    }


    @Override
    protected @NotNull JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        JButton edit = new JButton("Edit", Icon.EDIT);
        edit.addActionListener(e -> {
            onEdit();
        });

        JButton execute = new JButton("Execute");
        execute.addActionListener(e -> {
            saveFile();
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            doCancelAction();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.add(edit);
        panel.add(execute);
        panel.add(cancel);
        return panel;
    }

    private void onEdit() {
        MetaData metaData = new MetaData();
        metaData.setDescription(classNode.getNodeData().getName());
        metaData.setAlias(classNode.getNodeData().getAlias());
        if (classNode.getNodeData().getName() == null) {
            String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
            if (name == null || name.isEmpty()) {
                return;
            }
            metaData.setName(name);
        }
        ActionListener actionListener = new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            @SuppressWarnings("all")
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof List) {
                    List<Param> fields = (List<Param>) e.getSource();
                    if (fields.size() > 0) {
                        metaData.addFields(fields);
                        String result = velocityService.execute(ProjectKeys.ENTITY_TEMPLATE, metaData);
                        editor.setText(result);
                        classNode.getNodeData().setContent(result);
                    }

                }
            }
        };
        List<AbstractNode> nodes = classNode.getModel().getNodesByType(List.of(NodeType.ENTITY_NODE, NodeType.DATA_BASE));
        ChooseDialog chooseDialog = new ChooseDialog(ContextHelp.buildContext(nodes));
        chooseDialog.setListener(actionListener);
        chooseDialog.show();
    }


}
