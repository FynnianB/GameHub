package de.spindus.gamehub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {

	private Thread animator;

	private SpaceShip spaceShip;
	private List<Alien> aliens;

	private boolean ingame;

	private final int ICRAFT_X = 40;
	private final int ICRAFT_Y = 60;
	private final int DELAY = 10;

	private final int[][] pos = { { 2380, 29 }, { 2500, 59 }, { 1380, 89 }, { 780, 109 }, { 580, 139 }, { 680, 239 },
			{ 790, 259 }, { 760, 50 }, { 790, 150 }, { 980, 209 }, { 560, 45 }, { 510, 70 }, { 930, 159 }, { 590, 80 },
			{ 530, 60 }, { 940, 59 }, { 990, 30 }, { 920, 200 }, { 900, 259 }, { 660, 50 }, { 540, 90 }, { 810, 220 },
			{ 860, 20 }, { 740, 180 }, { 820, 128 }, { 490, 170 }, { 700, 30 } };

	public Board() {
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setBackground(Color.BLACK);
		setFocusable(true);
		ingame = true;

		setPreferredSize(new Dimension(GameHub.b_width, GameHub.b_height));

		spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);

		initAliens();
	}

	public void initAliens() {
		aliens = new ArrayList<>();
		for (int[] p : pos) {
			aliens.add(new Alien(p[0], p[1]));
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (ingame) {
			drawObjects(g);
		} else {
			drawGameOver(g);
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawObjects(Graphics g) {
		if (spaceShip.isVisible()) {
			g.drawImage(spaceShip.getImage(), spaceShip.getX(), spaceShip.getY(), this);
		}

		List<Missile> missiles = spaceShip.getMissiles();

		for (Missile m : missiles) {
			if (m.isVisible()) {
				g.drawImage(m.getImage(), m.getX(), m.getY(), this);
			}
		}

		for (Alien alien : aliens) {
			if (alien.isVisible()) {
				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString("Aliens left: " + aliens.size(), 5, 15);
	}

	private void drawGameOver(Graphics g) {
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fm = getFontMetrics(small);

		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(msg, (GameHub.b_width - fm.stringWidth(msg)) / 2, (GameHub.b_height - fm.getHeight()) / 2);
	}

	private void update() {
		updateMissiles();
		updateSpaceShip();
		updateAliens();

		checkCollisions();

		repaint();
	}

	private void updateMissiles() {
		List<Missile> missiles = spaceShip.getMissiles();

		for (int i = 0; i < missiles.size(); i++) {
			Missile missile = missiles.get(i);
			if (missile.isVisible()) {
				missile.move();
			} else {
				missiles.remove(i);
			}
		}
	}

	private void updateSpaceShip() {
		if (spaceShip.isVisible()) {
			spaceShip.move();
		}
	}

	private void updateAliens() {
		if (aliens.isEmpty()) {
			ingame = false;
			return;
		}
		for (int i = 0; i < aliens.size(); i++) {
			Alien a = aliens.get(i);

			if (a.isVisible()) {
				a.move();
			} else {
				aliens.remove(i);
			}
		}
	}

	public void checkCollisions() {
		Rectangle r3 = spaceShip.getBounds();
		for (Alien a : aliens) {
			Rectangle r2 = a.getBounds();
			if (r3.intersects(r2)) {
				spaceShip.setVisible(false);
				a.setVisible(false);
				ingame = false;
			}
		}
		List<Missile> ms = spaceShip.getMissiles();
		for (Missile m : ms) {
			Rectangle r1 = m.getBounds();
			for (Alien a : aliens) {
				Rectangle r2 = a.getBounds();
				if (r1.intersects(r2)) {
					m.setVisible(false);
					a.setVisible(false);
				}
			}
		}
	}

	@Override
	public void run() {
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();
		while (true) {

			update();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;
			if (sleep < 0) {
				sleep = 2;
			}
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				String msg = String.format("Thread interrupted: %s", e.getMessage());
				JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}
			beforeTime = System.currentTimeMillis();
		}
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			spaceShip.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			spaceShip.keyPressed(e);
		}
	}

}
