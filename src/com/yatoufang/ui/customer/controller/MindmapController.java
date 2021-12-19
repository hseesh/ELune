package com.yatoufang.ui.customer.controller;


import com.yatoufang.ui.customer.listener.PaintListener;
import com.yatoufang.ui.customer.model.Mindmap;
import com.yatoufang.ui.customer.model.Paintable;
import com.yatoufang.ui.customer.view.MindmapView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MindmapController extends MouseAdapter implements PaintListener {
	private MindmapView view;
	private Mindmap mindmap;
	
	public MindmapController(MindmapView view, Mindmap mindmap) {
		this.view = view;
		this.mindmap = mindmap;
	}

	@Override
	public void show(Paintable obj) {
		view.display(obj);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mindmap.drag(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mindmap.chooseNode(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//Do nothing
		mindmap.setDragNode(null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mindmap.clicked(e.getX(), e.getY());
	}
	
}
