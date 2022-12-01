package com.yatoufang.designer.component;

import com.intellij.util.ui.JBUI;
import com.yatoufang.designer.model.Element;


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
    private Element editingElement = null;


    public MainView() {

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




    @Override
    public void paintComponent(Graphics g) {
        Graphics2D brush = (Graphics2D) g.create();
          Canvas.drawBankGround(brush);
    }

}

