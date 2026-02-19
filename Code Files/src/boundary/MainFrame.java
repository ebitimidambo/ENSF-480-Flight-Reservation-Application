package boundary;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	private static MainFrame instance;
	
	private MainFrame() {
		setTitle("Flight Reservation Application - Group 16");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout());
	}
	
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		
		return instance;
	}
	
	public void showPanel(JPanel panel) {
		getContentPane().removeAll();
		add(panel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	public void showFrame() {
		setVisible(true);
	}
}
