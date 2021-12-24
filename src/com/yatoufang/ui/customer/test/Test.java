package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.MainView;
import com.yatoufang.test.component.MindMapEditor;
import com.yatoufang.ui.customer.controller.MindmapController;
import com.yatoufang.ui.customer.model.Mindmap;
import com.yatoufang.ui.customer.model.Node;
import com.yatoufang.ui.customer.model.Option;
import com.yatoufang.ui.customer.view.MindmapView;

import javax.swing.*;

public class Test {
	
	public static void main(String[] args) {
		JFrame frame=new JFrame("MindMap");
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MindMapEditor().getComponent());
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
