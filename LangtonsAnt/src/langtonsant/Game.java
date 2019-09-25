package langtonsant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import langtonsant.entity.Ant;

public class Game implements Runnable {

	private Display display;
	public int width, height;
	public String title;

	private BufferStrategy bs;
	private Graphics g;
	private boolean running = false;
	private Thread thread;

	private Ant ant;
	private int scale, spacing, antmargin;
	private String instructionset;
	private int[] mem;
	/*
	 * private final Color[] colors = { Color.DARK_GRAY, new Color(96, 57, 19),
	 * Color.gray, Color.WHITE, Color.blue, Color.RED, Color.green, Color.PINK,
	 * Color.magenta };
	 */
	private final Color[] colors = { Color.getHSBColor(0f, 0.9f, 0.7f), Color.getHSBColor(0.05555f, 0.9f, 0.7f),
			Color.getHSBColor(0.11111f, 0.9f, 0.7f), Color.getHSBColor(0.16666f, 0.9f, 0.7f),
			Color.getHSBColor(0.22222f, 0.9f, 0.7f), Color.getHSBColor(0.27777f, 0.9f, 0.7f),
			Color.getHSBColor(0.33333f, 0.9f, 0.7f), Color.getHSBColor(0.38888f, 0.9f, 0.7f),
			Color.getHSBColor(0.44444f, 0.9f, 0.7f), Color.getHSBColor(0.49999f, 0.9f, 0.7f),
			Color.getHSBColor(0.55555f, 0.9f, 0.7f), Color.getHSBColor(0.61111f, 0.9f, 0.7f),
			Color.getHSBColor(0.66666f, 0.9f, 0.7f), Color.getHSBColor(0.72222f, 0.9f, 0.7f),
			Color.getHSBColor(0.77777f, 0.9f, 0.7f), Color.getHSBColor(0.83333f, 0.9f, 0.7f),
			Color.getHSBColor(0.88888f, 0.9f, 0.7f), Color.getHSBColor(0.94444f, 0.9f, 0.7f),
			Color.getHSBColor(0.99999f, 0.9f, 0.7f) };

	public Game(String title, int width, int height, int scale, int spacing, int antmargin, String instructionset) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.spacing = spacing;
		this.antmargin = antmargin;
		this.instructionset = instructionset;

		mem = new int[width * height];
	}

	// Initialization
	private void init() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Color pixel at x,y
				mem[x + y * width] = Color.BLACK.getRGB();
			}
		}
		ant = new Ant(width / 2, height / 2, scale, spacing, antmargin, instructionset);
		display = new Display(title, width, height, ant);

	}

	// Threaded Start
	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	// Threaded stop
	public synchronized void stop() {
		if (!running)
			return;
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Game logic tick
	private void tick(long frameCount, double delta) {
		for (int i = 0; i < 1; i++) {
			mem = ant.updateAnt(mem, width, height, colors);
			ant.drawAnt(mem, width, height, colors);
		}
	}

	// Render tick
	private void render(long frameCount, double delta) throws Exception {

		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(1);
			bs = display.getCanvas().getBufferStrategy();
		}
		g = bs.getDrawGraphics();

		// g.setColor(Color.GREEN);
		// g.fill3DRect(100, 100, 200, 200, true);

		// ant.drawAnt(mem, width, height, colors);

		display.processImage(g, mem);

		bs.show();
		g.dispose();

		/*
		 * System.out.println("Frame: " + frameCount);
		 */
	}

	// Game loop
	public void run() {

		init();

		double fps = 5;
		double timePerTick = 1000000000.0 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long frameCount = 0;

		long timer = 0;
		@SuppressWarnings("unused")
		int ticks = 0;
		@SuppressWarnings("unused")
		double frameTime = 0;
		long frameNow = 1;
		long frameLast = 1;

		while (running) {

			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {

				frameTime = (frameNow - frameLast) / 1000.0 / 1000.0;
				frameLast = frameNow;
				frameNow = System.nanoTime();
				/*
				 * System.out.println("FrameTime: " + frameTime);
				 */

				try {

					tick(++frameCount, delta);

					render(frameCount, delta);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ticks++;
				delta--;
			}

			if (timer >= 1000000000) {

				System.out.println("FPS: " + ticks);

				ticks = 0;
				timer = 0;
			}
		}

		stop();
	}

}
