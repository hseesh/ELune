package com.yatoufang.ui.customer.view;


import com.yatoufang.ui.customer.model.Printable;

import javax.swing.*;
import java.awt.*;

public class MindmapView extends JLayeredPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8821924272283741078L;
	
	private Printable paintObj;

	public MindmapView(){
		setSize(800, 600);
	}
	
	public void display(Printable obj){
		this.paintObj=obj;
		repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g0=(Graphics2D)g;
		g0.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g0.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(paintObj!=null)
			paintObj.paint(g0);
	}
	
}
