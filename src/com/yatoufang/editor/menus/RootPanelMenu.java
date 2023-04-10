package com.yatoufang.editor.menus;


import com.intellij.icons.AllIcons;
import com.intellij.javaee.J2EEBundle;
import com.intellij.openapi.ui.Messages;
import com.yatoufang.editor.component.RootCanvas;
import com.yatoufang.editor.component.impl.DataBaseNode;
import com.yatoufang.editor.component.impl.EntityNode;
import com.yatoufang.editor.constant.CopyPasteHelper;
import com.yatoufang.editor.constant.FileHelper;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.ui.dialog.ConfigTemplateDialog;
import com.yatoufang.ui.dialog.EnumTemplateDialog;
import icons.Icon;

import javax.swing.*;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelMenu extends JPopupMenu {

    public RootPanelMenu(RootCanvas rootCanvas) {
        JMenu subMenuInsert = new JMenu("New");
        JMenuItem dateBaseNode = new JMenuItem("DateBase Node", Icon.NODE_DATA);
        JMenuItem entityNode = new JMenuItem("Entity Node", Icon.NODE_ENTITIES);
        JMenuItem configNode = new JMenuItem("Config Node", Icon.NODE_CONFIGURATION);
        JMenuItem enumNode = new JMenuItem("Enum Node", Icon.NODE_ENUM);
        dateBaseNode.addActionListener(e -> rootCanvas.getModel().add(new DataBaseNode(rootCanvas.getModel(), rootCanvas.getClickedPoint())));
        entityNode.addActionListener(e -> rootCanvas.getModel().add(new EntityNode(rootCanvas.getModel(), rootCanvas.getClickedPoint())));
        configNode.addActionListener(e -> new ConfigTemplateDialog(rootCanvas.getModel(), rootCanvas.getClickedPoint()).show());
        enumNode.addActionListener(e -> new EnumTemplateDialog(rootCanvas.getModel(), rootCanvas.getClickedPoint()).show());
        subMenuInsert.add(dateBaseNode);
        subMenuInsert.add(entityNode);
        subMenuInsert.add(configNode);
        subMenuInsert.add(enumNode);
        add(subMenuInsert);

        JMenu markAs = new JMenu("Mark as");
        JMenuItem game = new JMenuItem("Game", Icon.NODE_GAME);
        JMenuItem world = new JMenuItem("World", Icon.NODE_WORLD);
        JMenuItem battle = new JMenuItem("Battle", Icon.NODE_BATTLE);
        JMenuItem shard = new JMenuItem("Shard", Icon.NODE_SHARE);
        game.addActionListener(e -> rootCanvas.getModel().refreshWorkingSpace(ProjectKeys.GAME_SERVER));
        world.addActionListener(e -> rootCanvas.getModel().refreshWorkingSpace(ProjectKeys.WORLD_SERVER));
        battle.addActionListener(e -> rootCanvas.getModel().refreshWorkingSpace(ProjectKeys.BATTLE_SERVER));
        shard.addActionListener(e -> rootCanvas.getModel().refreshWorkingSpace(ProjectKeys.SHARD));
        markAs.add(game);
        markAs.add(shard);
        markAs.add(world);
        markAs.add(battle);
        add(markAs);

        JMenuItem execute = new JMenuItem("Execute");
        execute.addActionListener(e -> rootCanvas.getModel().onExecute(true, false));
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(execute);

        JMenuItem menuItemToggleConnectors = new JMenuItem("Show connectors", AllIcons.Ide.HectorOn);
        menuItemToggleConnectors.addActionListener(e -> rootCanvas.getModel().toggleConnectors());

        JMenuItem menuItemToggleGrid = new JMenuItem("Show grid", AllIcons.Actions.OpenNewTab);
        menuItemToggleGrid.addActionListener(e -> rootCanvas.getModel().toggleGrid());

        JMenuItem menuItemClearAll = new JMenuItem("Clear all", AllIcons.Actions.Cancel);
        menuItemClearAll.addActionListener(e -> {
            if (Messages.showYesNoDialog(Application.project, "CLear all", J2EEBundle.message("action.name.delete"), null) == Messages.YES) {
                rootCanvas.getModel().clear();
            }
        });

        JMenuItem menuItemCopy = new JMenuItem("Copy", AllIcons.Actions.Copy);
        menuItemCopy.addActionListener(e -> CopyPasteHelper.copy(rootCanvas.getModel()));

        JMenuItem menuItemPaste = new JMenuItem("Paste", AllIcons.Actions.MenuPaste);
        menuItemPaste.addActionListener(e -> CopyPasteHelper.paste(rootCanvas.getModel()));

        JMenuItem menuItemSave = new JMenuItem("Save", AllIcons.Actions.MenuSaveall);
        menuItemSave.addActionListener(e -> FileHelper.save(rootCanvas.getModel()));

        JMenuItem menuItemSaveAs = new JMenuItem("Save As", AllIcons.Actions.Menu_saveall);
        menuItemSaveAs.addActionListener(e -> FileHelper.saveAs(rootCanvas.getModel()));

        JMenuItem menuItemExport = new JMenuItem("Export png", AllIcons.Actions.Expandall);
        menuItemExport.addActionListener(e -> rootCanvas.exportAsImage());

        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemToggleConnectors);
        add(menuItemToggleGrid);
        add(menuItemClearAll);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemCopy);
        add(menuItemPaste);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemSave);
        add(menuItemSaveAs);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemExport);
    }
}