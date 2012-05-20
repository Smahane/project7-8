import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class App {

	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = screenSize.width;
		JFrame frame = new Frame(1280, 1024, 2 * w / 5, 0);
		frame.setVisible(true);

	}
}

class Frame extends JFrame {

	Frame(int width, int height, int x, int y) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Window");
		setSize(width, height);
		setLocation(x, y);

		add(new CustomComponent());
	}

	public void Display() {
		add(new CustomComponent());
	}

}

class CustomComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(100, 100);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 300);
	}

	@Override
	public void paintComponent(Graphics g) {

		Dimension dim = getSize();
		BufferedImage bi = new BufferedImage((int) dim.getWidth(),
				(int) dim.getHeight(), BufferedImage.TYPE_INT_ARGB);
		// E0875D
		// F2BE85
		// E0875D
		// E0873C
		Graphics2D g2 = bi.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0xE0873C));

		int rows = dim.height / 35;
		int index = 0;

		for (int j = 5; j < dim.height; j = j + rows) {
			int start = 0;
			index++;

			if (index % 2 == 0) {
				start = 25;
			} else {
				start = -1;
			}

			for (int i = start; i < dim.width; i = i + 50) {
				// System.out.println(i);
				Shape dot = new Ellipse2D.Float(i, j, 6, 6);
				g2.draw(dot);
				g2.fill(dot);
				// g.drawLine(i, 100, i, 100);
			}
		}

		try {
			ImageIO.write(bi, "PNG", new File("c:\\achtergrond.png"));
			System.out.println("Geschreven!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}