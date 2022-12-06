package com.yatoufang.ui.dialog.edit;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;
import com.yatoufang.editor.model.MetaData;
import com.yatoufang.editor.component.AbstractNode;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.Table;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.dialog.ChooseFieldsDialog;
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
import java.util.List;

/**
 * @author hse
 * @since 2022/11/11 0011
 */
public class EntityBuildDialog extends DialogWrapper {

    private EditorTextField editor;
    private VelocityService velocityService;

    private AbstractNode classNode;

    public EntityBuildDialog() {
        super(Application.project, true);
        velocityService = VelocityService.getInstance();
        editor = SwingUtils.createEditor("classNode.getNodeData().getContent()");
        editor.setFont(new Font(null, Font.PLAIN, 14));
        init();
    }

    public EntityBuildDialog(AbstractNode classNode) {
        super(Application.project, true);
        initData(classNode);
        init();
    }

    private void initData(AbstractNode classNode) {
        this.classNode = classNode;
        velocityService = VelocityService.getInstance();
        editor = SwingUtils.createEditor(classNode.getNodeData().getContent());
        editor.setFont(new Font(null, Font.PLAIN, 14));
    }

    private void saveFile() {
        classNode.getNodeData().setContent(editor.getText());
        String workPath = classNode.getModel().getBasePath();
        String moduleName = classNode.getModel().getModuleName();
        if (workPath == null || moduleName == null) {
            return;
        }
        String filePath = StringUtil.buildPath(workPath, ProjectKeys.MODULE, moduleName, ProjectKeys.MODEL, classNode.getName() + ProjectKeys.JAVA);
        FileWrite.write(classNode.getNodeData().getContent(), filePath, true, false);
    }


    @Override
    public void doCancelAction() {
        super.doCancelAction();
        if (classNode == null) {
            return;
        }
        classNode.getNodeData().refresh(editor.getText());
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
            MetaData metaData = classNode.getNodeData().getMetaData();
            if (classNode.getNodeData().getName() == null) {
                String name = Messages.showInputDialog(NotifyKeys.INPUT, NotifyKeys.INPUT_TITLE, null);
                if (name == null || name.isEmpty()) {
                    return;
                }
                metaData.setName(classNode.getNodeData().getName());
            }
            ChooseFieldsDialog chooseFieldsDialog = new ChooseFieldsDialog(new Table());
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
            chooseFieldsDialog.setListener(actionListener);
            chooseFieldsDialog.show();
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
}
