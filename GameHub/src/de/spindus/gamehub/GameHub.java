package de.spindus.gamehub;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class GameHub extends JFrame{
	
	public static int b_width = 400;
	public static int b_height = 300;
	
	public GameHub() {
		initUI();
	}
	
	private void initUI() {
		add(new Board());
		
		setSize(b_width, b_height);
		
		setTitle("GameHub");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			GameHub ex = new GameHub();
			ex.setVisible(true);
		});
	}
	
}
