package de.spindus.gamehub.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import de.spindus.gamehub.GameHub;

public class Snake extends JPanel implements ActionListener {

	private final int dot_size = 10;
	private final int all_dots = 900;
	private final int rand_pos = 29;
	private final int delay = 140;

	private final int x[] = new int[all_dots];
	private final int y[] = new int[all_dots];

	private int dots;
	private int apple_x;
	private int apple_y;

	private boolean leftDir = false;
	private boolean rightDir = true;
	private boolean upDir = false;
	private boolean downDir = false;
	private boolean ingame = true;

	private Timer timer;
	private Image ball;
	private Image apple;
	private Image head;

	public Snake() {
		setBackground(Color.BLACK);
		setFocusable(true);
		requestFocusInWindow();
		setPreferredSize(new Dimension(GameHub.b_width, GameHub.b_height));
		loadImages();
		initGame();
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
		this.getActionMap().put("Left", leftPressed);
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
		this.getActionMap().put("Right", rightPressed);
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
		this.getActionMap().put("Up", upPressed);
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
		this.getActionMap().put("Down", downPressed);
	}

	private void loadImages() {
		ImageIcon iid = new ImageIcon("resources/images/snake/dot.png");
		ball = iid.getImage();

		ImageIcon iia = new ImageIcon("resources/images/snake/apple.png");
		apple = iia.getImage();

		ImageIcon iih = new ImageIcon("resources/images/snake/head.png");
		head = iih.getImage();
	}

	private void initGame() {
		dots = 3;

		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}

		locateApple();

		timer = new Timer(delay, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		draw(g);
	}

	private void draw(Graphics g) {
		if (ingame) {
			g.drawImage(apple, apple_x, apple_y, this);
			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(head, x[z], y[z], this);
				} else {
					g.drawImage(ball, x[z], y[z], this);
				}
			}
			Toolkit.getDefaultToolkit().sync();
		} else {
			drawGameOver(g);
		}
	}

	private void drawGameOver(Graphics g) {
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fm = getFontMetrics(small);

		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(msg, (GameHub.b_width - fm.stringWidth(msg)) / 2, (GameHub.b_height - fm.getHeight()) / 2);
	}

	private void checkApple() {
		if ((x[0] == apple_x) && (y[0] == apple_y)) {
			dots++;
			locateApple();
		}
	}

	private void move() {
		for (int z = dots; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}
		if (leftDir) {
			x[0] -= dot_size;
		}
		if (rightDir) {
			x[0] += dot_size;
		}

		if (upDir) {
			y[0] -= dot_size;
		}

		if (downDir) {
			y[0] += dot_size;
		}
	}

	private void checkCollision() {
		for (int z = dots; z > 0; z--) {
			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				ingame = false;
			}
		}
		if (y[0] >= GameHub.b_height) {
			ingame = false;
		}
		if (y[0] < 0) {
			ingame = false;
		}
		if (x[0] >= GameHub.b_width) {
			ingame = false;
		}
		if (x[0] < 0) {
			ingame = false;
		}
		if (!ingame) {
			timer.stop();
		}
	}

	private void locateApple() {
		int r = (int) (Math.random() * rand_pos);
		apple_x = ((r * dot_size));

		r = (int) (Math.random() * rand_pos);
		apple_y = ((r * dot_size));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ingame) {
			checkApple();
			checkCollision();
			move();
		}
		repaint();
	}

	private Action leftPressed = new AbstractAction("left") {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!rightDir) {
				leftDir = true;
				upDir = false;
				downDir = false;
			}
		}
	};

	private Action rightPressed = new AbstractAction("right") {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!leftDir) {
				rightDir = true;
				upDir = false;
				downDir = false;
			}
		}
	};

	private Action upPressed = new AbstractAction("up") {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!downDir) {
				upDir = true;
				rightDir = false;
				leftDir = false;
			}
		}
	};

	private Action downPressed = new AbstractAction("down") {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!upDir) {
				downDir = true;
				rightDir = false;
				leftDir = false;
			}
		}
	};
}
