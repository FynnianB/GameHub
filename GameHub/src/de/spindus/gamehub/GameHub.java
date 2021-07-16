package de.spindus.gamehub;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.spindus.gamehub.menu.MenuPanel;
import de.spindus.gamehub.puzzle.Puzzle;
import de.spindus.gamehub.snake.Snake;

public class GameHub extends JFrame {

	public static int b_width = 1280;
	public static int b_height = 720;

	public static CardLayout cardLayout;
	public static JPanel mainPanel;
	MenuPanel menu;
	Puzzle puzzle;
	Snake snake;

	public GameHub() {
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		menu = new MenuPanel();
		puzzle = new Puzzle();
		snake = new Snake();

		mainPanel.add(menu, "menu");
		mainPanel.add(puzzle, "puzzle");
		mainPanel.add(snake, "snake");

		add(mainPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setTitle("GameHub");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GameHub gameHub = new GameHub();
			}
		});
	}

	public static void changeBoard(GameMode gm) {
		if (gm == GameMode.PUZZLE) {
			cardLayout.show(mainPanel, "puzzle");
		} else if (gm == GameMode.SNAKE) {
			cardLayout.show(mainPanel, "snake");
		}
	}

}
