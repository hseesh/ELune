package com.yatoufang.test.controller;

import com.yatoufang.test.component.MainView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/26 0026
 */
public class MyDocumentListener implements DocumentListener {

    private final JTextArea textEditor;
    private final JPanel textEditorPanel;
    private final MainView mainView;

    public MyDocumentListener(JTextArea textEditor, JPanel textEditorPanel, MainView mainView) {
        this.textEditor = textEditor;
        this.textEditorPanel = textEditorPanel;
        this.mainView = mainView;

    }

    public void updateEditorPanelSize(Dimension newSize) {

        final Dimension editorPanelMinSize = textEditorPanel.getMinimumSize();
        final Dimension newDimension = new Dimension(Math.max(editorPanelMinSize.width, newSize.width), Math.max(editorPanelMinSize.height, newSize.height));

        final Rectangle editorBounds = textEditorPanel.getBounds();
        editorBounds.setSize(newDimension);

        final Rectangle mainPanelBounds = mainView.getBounds();

        if (!mainPanelBounds.contains(editorBounds)) {
            double ex = editorBounds.getX();
            double ey = editorBounds.getY();
            double ew = editorBounds.getWidth();
            double eh = editorBounds.getHeight();

            if (ex < 0.0d) {
                ew -= ex;
                ex = 0.0d;
            }
            if (ey < 0.0d) {
                eh -= ey;
                ey = 0.0d;
            }

            if (ex + ew > mainPanelBounds.getWidth()) {
                ex = mainPanelBounds.getWidth() - ew;
            }
            if (ey + eh > mainPanelBounds.getHeight()) {
                ey = mainPanelBounds.getHeight() - eh;
            }

            editorBounds.setRect(ex, ey, ew, eh);
        }

        textEditorPanel.setBounds(editorBounds);
        textEditorPanel.repaint();
    }


    public void callUpdateEditorPanelSize() {
        SwingUtilities.invokeLater(() -> updateEditorPanelSize(textEditor.getPreferredSize()));
    }


    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        callUpdateEditorPanelSize();
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        callUpdateEditorPanelSize();
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        callUpdateEditorPanelSize();
    }
}
