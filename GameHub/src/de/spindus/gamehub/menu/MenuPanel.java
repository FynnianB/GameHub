package de.spindus.gamehub.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.spindus.gamehub.GameHub;
import de.spindus.gamehub.GameMode;

public class MenuPanel extends JPanel implements ActionListener {

	public MenuPanel() {
		setBackground(Color.DARK_GRAY);
		setFocusable(true);
		setPreferredSize(new Dimension(GameHub.b_width, GameHub.b_height));

		JButton puzzleBtn = new JButton("Play Puzzle");
		puzzleBtn.addActionListener(this);
		add(puzzleBtn, BorderLayout.SOUTH);

		JButton snakeBtn = new JButton("Play Snake");
		snakeBtn.addActionListener(this);
		add(snakeBtn, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String title = e.getActionCommand();
		if (title == "Play Puzzle") {
			GameHub.changeBoard(GameMode.PUZZLE);
		} else if (title == "Play Snake") {
			GameHub.changeBoard(GameMode.SNAKE);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawObjects(g);
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawObjects(Graphics g) {
		String msg = "Menu";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fm = getFontMetrics(small);

		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(msg, (GameHub.b_width - fm.stringWidth(msg)) / 2, Math.round(GameHub.b_height * 0.1F));
	}

}
