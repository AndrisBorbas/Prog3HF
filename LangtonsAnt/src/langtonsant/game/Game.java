package langtonsant.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import langtonsant.entity.Ant;

public class Game implements Runnable {

	private Display display;
	public int width, height;
	public String title;

	protected BufferStrategy bs;
	protected Graphics g;
	private boolean running = false;
	private Thread thread;

	private Ant ant;
	// private Ant ant2;
	private int scale, spacing, antmargin;
	private String instructionset;
	protected int[] mem;
	/*
	 * private final Color[] colors = { Color.DARK_GRAY, new Color(96, 57, 19),
	 * Color.gray, Color.WHITE, Color.blue, Color.RED, Color.green, Color.PINK,
	 * Color.magenta };
	 */
	private Color[] colors = { Color.getHSBColor(0f, 0.9f, 0.7f), Color.getHSBColor(0.05555f, 0.9f, 0.7f),
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

		// ant2 = new Ant(width / 8 * 5 - 5, height / 2, scale, spacing, antmargin,
		// instructionset);

		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(1);
			bs = display.getCanvas().getBufferStrategy();
		}

	}

	// Threaded Start
	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.setName("Game");
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
	private void tick(int stepper) {
		for (int i = 0; i < stepper; i++) {
			mem = ant.updateAnt(mem, width, height, colors);
			ant.drawAnt(mem, width, height, colors);
			// mem = ant2.updateAnt(mem, width, height, colors);
			// ant2.drawAnt(mem, width, height, colors);
		}
	}

	// Render tick
	private void render(String FPSs, String UPSs) throws Exception {

		/*
		 * bs = display.getCanvas().getBufferStrategy(); if (bs == null) {
		 * display.getCanvas().createBufferStrategy(1); bs =
		 * display.getCanvas().getBufferStrategy(); } g = bs.getDrawGraphics();
		 */

		g = bs.getDrawGraphics();

		g.setColor(Color.GREEN);
		g.drawString(FPSs, 100, 100);
		g.setColor(Color.RED);
		g.drawString(UPSs, 100, 120);
		// g.fill3DRect(100, 100, 200, 200, true);

		display.processImage(g, mem);

		g.setColor(Color.GREEN);
		g.drawString(FPSs, 100, 100);
		g.setColor(Color.RED);
		g.drawString(UPSs, 100, 120);

		bs.show();
		g.dispose();
	}

	public class UpdateThread extends TickThread {

		public UpdateThread(double tickRate, int[] mem, String name) {
			super(tickRate, mem, name);
		}

		public void run() {
			now = System.nanoTime();
			while (true) {

				try {

					for (int i = 0; i < 1; i++) {
						super.mem = ant.updateAnt(super.mem, width, height, colors);
						ant.drawAnt(super.mem, width, height, colors);
						// super.mem = ant2.updateAnt(super.mem, width, height, colors);
						// ant2.drawAnt(super.mem, width, height, colors);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(701);
				}
				super.run();
			}
		}
	}

	public class RenderThread extends TickThread {

		private UpdateThread ut;

		public RenderThread(double tickRate, int[] mem, String name, UpdateThread ut) {
			super(tickRate, mem, name);
			this.ut = ut;
		}

		public void run() {
			now = System.nanoTime();
			while (true) {

				try {

					mem = ut.getMem();

					g = bs.getDrawGraphics();

					display.processImage(g, mem);

					bs.show();
					g.dispose();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(701);
				}

				super.run();
			}

		}
	}

	// Game loop
	public void run() {

		init();

		// update rate limiter
		double tickRate = 1200;
		double timePerTick = 1000000000.0 / tickRate;
		double tickDelta = 0;

		// framerate limiter
		double frameRate = 60;
		double timePerFrame = 1000000000.0 / frameRate;
		double frameDelta = 0;

		// delta timer
		long now;
		long lastTime = System.nanoTime();
		@SuppressWarnings("unused")
		long frameCount = 0L;

		// framerate and update rate counters
		long timer = 0L;
		int ticks = 0;
		int frames = 0;

		// frametime counter
		@SuppressWarnings("unused")
		double frameTime = 0;
		long frameNow = 1L;
		long frameLast = 1L;

		@SuppressWarnings("unused")
		int exit = 0;

		String FPSs = new String();
		String UPSs = new String();

		UpdateThread updateThread = new UpdateThread(1000, mem, "UpdateThread");
		RenderThread renderThread = new RenderThread(60, mem, "RenderThread", updateThread);

		updateThread.start();
		renderThread.start();

		while (running) {

			now = System.nanoTime();
			tickDelta += (now - lastTime) / timePerTick;
			frameDelta += (now - lastTime) / timePerFrame;
			timer += now - lastTime;
			lastTime = now;

			/*
			 * 
			 * Game input
			 * 
			 */

			if (tickDelta >= 1) {

				try {

					// tick(1);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(701);
				}

				ticks++;
				tickDelta--;
			}

			if (frameDelta >= 1) {

				frameTime = (frameNow - frameLast) / 1000000.0;
				frameLast = frameNow;
				frameNow = System.nanoTime();
				/*
				 * System.out.println("FrameTime: " + frameTime);
				 */

				try {
					// mem = updateThread.getMemm();
					// render(FPSs, UPSs);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(701);
				}

				frameCount++;
				frames++;
				frameDelta--;
			}

			if (timer >= 1000000000) {
				// System.out.println("FPS: " + frames + ", UPS: " + ticks);
				FPSs = String.valueOf(frames);
				UPSs = String.valueOf(ticks);
				ticks = 0;
				frames = 0;
				timer = 0;
				// if(++exit==10) System.exit(101);
			}
		}

		stop();
	}

}
