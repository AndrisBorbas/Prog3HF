package langtonsant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class DrawBoard extends javax.swing.JPanel {

	private Image src = null;
	private int width, height;
	private Worker worker;

	public DrawBoard(int width, int height) {
		this.width = width;
		this.height = height;
		worker = new Worker();

		worker.execute();
	}

	public void redraw() {
		// worker.execute();
		try {
			worker.doInBackground();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (src != null)
			g.drawImage(src, 0, 0, this);
	}

	private class Worker extends SwingWorker<Void, Image> {
		private final Color[] colors = { Color.gray, Color.gray, Color.gray, Color.white };

		protected void process(List<Image> chunks) {
			for (Image bufferedImage : chunks) {
				src = bufferedImage;
				repaint();
			}
		}

		protected Void doInBackground() throws Exception {
			Random rand = new Random();
			int[] mem = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					// Color pixel at x,y
					mem[x + y * width] = colors[rand.nextInt(4)].getRGB();
				}
			}
			/*
			 * System.out.println("draw");
			 */
			Image img = createImage(new MemoryImageSource(width, height, mem, 0, width));
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			publish(bi);
			return null;
		}
	}
}
