package com.yatoufang.editor.menus;


import com.intellij.icons.AllIcons;
import com.intellij.javaee.J2EEBundle;
import com.intellij.openapi.ui.Messages;
import com.yatoufang.editor.component.Canvas;
import com.yatoufang.editor.component.impl.ConfigNode;
import com.yatoufang.editor.component.impl.DataBaseNode;
import com.yatoufang.editor.component.impl.EntityNode;
import com.yatoufang.editor.constant.CopyPasteHelper;
import com.yatoufang.editor.constant.FileManager;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import icons.Icon;

import javax.swing.*;

/**
 * @author hse
 * @since 2022/9/23 0023
 */
public class RootPanelMenu extends JPopupMenu {

    public RootPanelMenu(Canvas canvas) {


        JMenu subMenuInsert = new JMenu("New");
        JMenuItem dateBaseNode = new JMenuItem("DateBase Node", Icon.NODE_DATA_BASE);
        JMenuItem entityNode = new JMenuItem("Entity Node", Icon.NODE_ENTITY);
        JMenuItem configNode = new JMenuItem("Config Node", Icon.NODE_SETTING);
        dateBaseNode.addActionListener(e -> canvas.getModel().add(new DataBaseNode(canvas.getModel(), canvas.getClickedPoint())));
        entityNode.addActionListener(e -> canvas.getModel().add(new EntityNode(canvas.getModel(), canvas.getClickedPoint())));
        configNode.addActionListener(e -> canvas.getModel().add(new ConfigNode(canvas.getModel(), canvas.getClickedPoint())));
        subMenuInsert.add(dateBaseNode);
        subMenuInsert.add(entityNode);
        subMenuInsert.add(configNode);
        add(subMenuInsert);

        JMenu markAs = new JMenu("Mark as");
        JMenuItem game = new JMenuItem("Game", Icon.NODE_DATA_BASE);
        JMenuItem world = new JMenuItem("World", Icon.NODE_DATA_BASE);
        JMenuItem battle = new JMenuItem("Battle", Icon.NODE_DATA_BASE);
        JMenuItem shard = new JMenuItem("Shard", Icon.NODE_DATA_BASE);
        game.addActionListener(e -> canvas.getModel().refreshWorkingSpace(ProjectKeys.GAME_SERVER));
        world.addActionListener(e -> canvas.getModel().refreshWorkingSpace(ProjectKeys.WORLD_SERVER));
        battle.addActionListener(e -> canvas.getModel().refreshWorkingSpace(ProjectKeys.BATTLE_SERVER));
        shard.addActionListener(e -> canvas.getModel().refreshWorkingSpace(ProjectKeys.SHARD));
        markAs.add(game);
        markAs.add(world);
        markAs.add(battle);
        markAs.add(shard);
        add(markAs);

        JMenuItem execute = new JMenuItem("Execute");
        execute.addActionListener(e -> canvas.getModel().onExecute());
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(execute);

        JMenuItem menuItemToggleConnectors = new JMenuItem("Show connectors",AllIcons.Ide.HectorOn);
        menuItemToggleConnectors.addActionListener(e -> canvas.getModel().toggleConnectors());

        JMenuItem menuItemToggleGrid = new JMenuItem("Show grid", AllIcons.Actions.OpenNewTab);
        menuItemToggleGrid.addActionListener(e -> canvas.getModel().toggleGrid());

        JMenuItem menuItemClearAll = new JMenuItem("Clear all", AllIcons.Actions.Cancel);
        menuItemClearAll.addActionListener(e -> {
            if (Messages.showYesNoDialog(Application.project, "CLear all", J2EEBundle.message("action.name.delete"), null) == Messages.YES) {
                canvas.getModel().clear();
            }
        });

        JMenuItem menuItemCopy = new JMenuItem("Copy", AllIcons.Actions.Copy);
        menuItemCopy.addActionListener(e -> CopyPasteHelper.copy(canvas.getModel()));

        JMenuItem menuItemPaste = new JMenuItem("Paste", AllIcons.Actions.MenuPaste);
        menuItemPaste.addActionListener(e -> CopyPasteHelper.paste(canvas.getModel()));

        JMenuItem menuItemSave = new JMenuItem("Save", AllIcons.Actions.MenuSaveall);
        menuItemSave.addActionListener(e -> FileManager.save(canvas.getModel()));

        JMenuItem menuItemSaveAs = new JMenuItem("Save As", AllIcons.Actions.Menu_saveall);
        menuItemSaveAs.addActionListener(e -> FileManager.saveAs(canvas.getModel()));

        JMenuItem menuItemOpen = new JMenuItem("Open", AllIcons.Actions.MenuOpen);
        menuItemOpen.addActionListener(e -> canvas.openFile());

        JMenuItem menuItemExport = new JMenuItem("Export png", AllIcons.Actions.Expandall);
        menuItemExport.addActionListener(e -> canvas.exportAsImage());

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
        add(menuItemOpen);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemExport);
    }
}