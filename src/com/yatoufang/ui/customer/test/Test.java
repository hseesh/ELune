package com.yatoufang.ui.customer.test;

import com.yatoufang.test.component.MainView;
import com.yatoufang.test.component.MindMapEditor;
import com.yatoufang.ui.customer.controller.MindmapController;
import com.yatoufang.ui.customer.model.Mindmap;
import com.yatoufang.ui.customer.model.Node;
import com.yatoufang.ui.customer.model.Option;
import com.yatoufang.ui.customer.view.MindmapView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		new Test();
	}

	public Test() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				JFrame frame = new JFrame("Testing");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new TestPane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public class TestPane extends JPanel {

		private JLabel map;

		public TestPane() {
			setLayout(new BorderLayout());
			try {
				map = new JLabel(new ImageIcon(ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\2Ybi2q.jpg"))));
				map.setAutoscrolls(true);
				add(new JScrollPane(map));

				MouseAdapter ma = new MouseAdapter() {

					private Point origin;

					@Override
					public void mousePressed(MouseEvent e) {
						origin = new Point(e.getPoint());
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						if (origin != null) {
							JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, map);
							if (viewPort != null) {
								int deltaX = origin.x - e.getX();
								int deltaY = origin.y - e.getY();

								Rectangle view = viewPort.getViewRect();
								view.x += deltaX;
								view.y += deltaY;

								map.scrollRectToVisible(view);
							}
						}
					}

				};

				map.addMouseListener(ma);
				map.addMouseMotionListener(ma);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(200, 200);
		}

	}
}
