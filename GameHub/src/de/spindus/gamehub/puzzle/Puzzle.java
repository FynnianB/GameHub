package de.spindus.gamehub.puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.spindus.gamehub.GameHub;

public class Puzzle extends JPanel {

	private BufferedImage source;
	private BufferedImage resized;
	private Image image;
	private PuzzleBtn lastBtn;
	private int width, height;

	private List<PuzzleBtn> btns;
	private List<Point> solution;

	private final int number_of_buttons = 12;
	private final int desired_width = GameHub.b_width;

	public Puzzle() {

		solution = new ArrayList<>();
		solution.add(new Point(0, 0));
		solution.add(new Point(0, 1));
		solution.add(new Point(0, 2));
		solution.add(new Point(1, 0));
		solution.add(new Point(1, 1));
		solution.add(new Point(1, 2));
		solution.add(new Point(2, 0));
		solution.add(new Point(2, 1));
		solution.add(new Point(2, 2));
		solution.add(new Point(3, 0));
		solution.add(new Point(3, 1));
		solution.add(new Point(3, 2));

		btns = new ArrayList<>();

		setBorder(BorderFactory.createLineBorder(Color.gray));
		setLayout(new GridLayout(4, 3, 0, 0));
		setBackground(Color.DARK_GRAY);
		setFocusable(true);
		setPreferredSize(new Dimension(GameHub.b_width, GameHub.b_height));

		try {
			source = loadImage();
			int h = getNewHeight(source.getWidth(), source.getHeight());
			resized = resizeImage(source, desired_width, h, BufferedImage.TYPE_INT_ARGB);
		} catch (IOException ex) {
			System.err.print(ex);
		}

		width = resized.getWidth(null);
		height = resized.getHeight(null);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				image = createImage(new FilteredImageSource(resized.getSource(),
						new CropImageFilter(j * width / 3, i * height / 4, (width / 3), height / 4)));

				PuzzleBtn btn = new PuzzleBtn(image);
				btn.putClientProperty("position", new Point(i, j));

				if (i == 3 && j == 2) {
					lastBtn = new PuzzleBtn();
					lastBtn.setBorderPainted(false);
					lastBtn.setContentAreaFilled(false);
					lastBtn.setLastButton();
					lastBtn.putClientProperty("position", new Point(i, j));
				} else {
					btns.add(btn);
				}
			}
		}
		Collections.shuffle(btns);
		btns.add(lastBtn);

		for (int i = 0; i < number_of_buttons; i++) {
			PuzzleBtn btn = btns.get(i);
			add(btn);
			btn.setBorder(BorderFactory.createLineBorder(Color.gray));
			btn.addActionListener(new ClickAction());
		}
	}

	private int getNewHeight(int w, int h) {
		double ratio = desired_width / (double) w;
		int newHeight = (int) (h * ratio);
		return newHeight;
	}

	private BufferedImage loadImage() throws IOException {
		BufferedImage bimg = ImageIO.read(new File("images/puzzle/img.jpg"));
		return bimg;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException {
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}

	private class ClickAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			checkButton(e);
			checkSolution();
		}

		private void checkButton(ActionEvent e) {
			int lidx = 0;
			for (PuzzleBtn btn : btns) {
				if (btn.isLastButton()) {
					lidx = btns.indexOf(btn);
				}
			}

			JButton btn = (JButton) e.getSource();
			int bidx = btns.indexOf(btn);

			if ((bidx - 1 == lidx) || (bidx + 1 == lidx) || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
				Collections.swap(btns, lidx, bidx);
				updateButtons();
			}
		}

		private void updateButtons() {
			removeAll();
			for (JComponent btn : btns) {
				add(btn);
			}
			validate();
		}
	}

	private void checkSolution() {
		List<Point> current = new ArrayList<>();
		for (JComponent btn : btns) {
			current.add((Point) btn.getClientProperty("position"));
		}
		if (compareList(solution, current)) {
			JOptionPane.showMessageDialog(this, "Finished", "Congratulation", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static boolean compareList(List<Point> l1, List<Point> l2) {
		return l1.toString().contentEquals(l2.toString());
	}

}
