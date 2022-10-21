package com.yatoufang.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import com.yatoufang.templet.Application;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Set;

/**
 * @author GongHuang（hse）
 * @since 2022/10/20
 */
public class TextChooseDialog extends DialogWrapper implements KeyListener {

    private final JBList<String> list;

    private final Set<String> selectLists;

    private final DefaultListModel<String> listModel;

    public TextChooseDialog(JBList<String> list, Set<String> resultSet, DefaultListModel<String> listModel, String title) {
        super(Application.project);
        this.list = list;
        this.listModel = listModel;
        this.selectLists = resultSet;
        init();
        setTitle(title);
        list.requestFocus();
        list.addKeyListener(this);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return list;
    }

    @NotNull
    @Override
    protected JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
        return new JPanel();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            int[] indices = list.getSelectedIndices();
            for (int index : indices) {
                String elementAt = listModel.getElementAt(index);
                selectLists.add(elementAt);
            }
            dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
