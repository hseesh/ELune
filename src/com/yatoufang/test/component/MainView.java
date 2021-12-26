package com.yatoufang.test.component;

import com.intellij.util.ui.JBUI;
import com.yatoufang.test.model.AbstractElement;
import com.yatoufang.test.model.Topic;

import javax.swing.*;
import java.awt.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/22
 */
public class MainView extends JComponent {

    private Crayons crayons;
    private final JTextArea textEditor = new JTextArea();
    private final JPanel textEditorPanel = new JPanel();
    private AbstractElement editingElement = null;
    private final Topic rootTopic;

    public MainView() {
        rootTopic = new Topic("New Topic");
        textEditorPanel.setLayout(new BorderLayout(10, 10));

        textEditor.setMargin(JBUI.insets(5));
        textEditor.setBorder(BorderFactory.createEtchedBorder());
        textEditor.setTabSize(4);
        textEditorPanel.add(textEditor, BorderLayout.CENTER);
        textEditorPanel.setVisible(true);
        textEditor.setText("Hello Customer Component");
        textEditor.setMinimumSize(new Dimension(50,100));
        add(textEditorPanel);
    }

    public void startEdit(AbstractElement element) {
        if (element == null) {
            editingElement = null;
            textEditorPanel.setVisible(false);
        } else {
            editingElement = element;
            element.fillByTextAndFont(textEditor);
            final Dimension textBlockSize = new Dimension((int) element.getBounds().getWidth(), (int) element.getBounds().getHeight());
            textEditorPanel.setBounds((int) element.getBounds().getX(), (int) element.getBounds().getY(), textBlockSize.width, textBlockSize.height);
            textEditor.setMinimumSize(textBlockSize);
            textEditorPanel.setVisible(true);
            textEditor.requestFocus();
        }
    }

    public boolean endEdit(final boolean commit) {
        boolean result = false;

        result = editingElement != null;

        if (editingElement != null) {
            final AbstractElement editedElement = editingElement;
            Topic editedTopic = editingElement.getTopic();

            final int[] pathToEditedTopic = editedTopic.getPositionPath();

            if (commit) {


                final String oldText = editedElement.getText();
                String newText = textEditor.getText();


                boolean contentChanged = false;
                if (!oldText.equals(newText)) {
                    editedElement.setText(newText);
                    contentChanged = true;
                }
                textEditorPanel.setVisible(false);

                doLayout();
                revalidate();
                repaint();

            }
        }
        return result;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D brush = (Graphics2D) g.create();
          Canvas.drawBankGround(brush);
    }

    public Topic getRootTopic() {
        return rootTopic;
    }
}

