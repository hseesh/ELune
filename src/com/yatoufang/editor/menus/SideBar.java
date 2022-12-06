package com.yatoufang.editor.menus;

import com.yatoufang.editor.component.Canvas;
import com.yatoufang.editor.constant.CopyPasteHelper;
import com.yatoufang.editor.constant.FileManager;
import com.yatoufang.editor.constant.IconManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;


public class SideBar extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel zoomFactor = new JLabel();

    public SideBar(Canvas canvas) {
        setPreferredSize(new Dimension(40, getPreferredSize().height));

        add(new Button("Undo", IconManager.getIcon("32/undo"), () ->
                canvas.getModel().undo()
        ));

        add(new Button("Redo", IconManager.getIcon("32/redo"), () ->
                canvas.getModel().redo()
        ));

        add(new Button("Save", IconManager.getIcon("32/save"), () ->
                FileManager.save(canvas.getModel())
        ));

        add(new Button("Open", IconManager.getIcon("32/open"), () ->
                canvas.getRootPanel().getDrawingSurface().openFile()
        ));

        add(new Button("Copy", IconManager.getIcon("32/copy"), () ->
                CopyPasteHelper.copy(canvas.getModel())
        ));

        add(new Button("Paste", IconManager.getIcon("32/paste"), () ->
                CopyPasteHelper.paste(canvas.getModel())
        ));

        add(new Button("Export as PNG", IconManager.getIcon("32/export"), () ->
                canvas.getRootPanel().getDrawingSurface().exportAsImage()
        ));

        add(new Button("Clear", IconManager.getIcon("32/trash_bin"), () -> {
//            if (JOptionPane.showConfirmDialog(Editor.window, "Clear all boxes and lines?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
//                canvas.getModel().clear();
//            }
        }));

        add(new Button("Zoom-in", IconManager.getIcon("32/zoom_in"), () -> {
            canvas.getRootPanel().getZoomPanel().zoomIn();
            updateZoomLabel(canvas);
        }));

        add(new Button("Zoom-out", IconManager.getIcon("32/zoom_out"), () -> {
            canvas.getRootPanel().getZoomPanel().zoomOut();
            updateZoomLabel(canvas);
        }));

        updateZoomLabel(canvas);
        zoomFactor.setFont(new Font("Serif", Font.PLAIN, 10));
        add(zoomFactor);
    }

    public void updateZoomLabel(Canvas canvas) {
        double percent = canvas.getRootPanel().getZoomPanel().getScale() * 100;
        zoomFactor.setText(new DecimalFormat("#").format(percent) + "%");
    }

    private static final class Button extends JPanel {
        private static final long serialVersionUID = 1L;

        private Button(String toolTipText, ImageIcon icon, Runnable r) {
            setPreferredSize(new Dimension(50, 40));
            setToolTipText(toolTipText);
            add(new JLabel(icon));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    r.run();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
        }
    }
}