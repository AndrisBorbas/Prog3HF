package langtonsant.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import langtonsant.Settings;
import langtonsant.entity.Ant;

public class Game implements Runnable/* , KeyEventDispatcher */ {

	public Display display;
	public int width, height;
	public String title;

	int steps = 0;
	static int runs = 0;

	public static int saves = 0;

	protected BufferStrategy bs;
	protected Graphics g;
	// private boolean running = false;
	public Thread thread;

	UpdateThread updateThread;
	RenderThread renderThread;

	private Ant ant;
	private Ant ant2;
	private int scale, spacing, antmargin;
	private String instructionset;
	protected int[] mem;

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

	public Game(String title, int width, int height, int scale, int spacing, int antmargin, String instructionset)
			throws IOException {
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
		// KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

		clearMem();

		ant = new Ant(width / 2, height / 2, scale, spacing, antmargin, instructionset);
		display = new Display(title, this, width, height, mem);

		ant2 = new Ant(width / 8 * 5 - 6, height / 2, scale, spacing, antmargin, instructionset);

		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(2);
			bs = display.getCanvas().getBufferStrategy();
		}

	}

	// Threaded Start
	public synchronized void start() {
		thread = new Thread(this);
		thread.setName("Game" + (runs++));
		thread.start();
	}

	// Threaded stop
	public synchronized void stop() {
		try {
			updateThread.join();

			renderThread.join();

			display.dispose();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Updates the ant by the amount specified in <i>stepper</i>.
	 * 
	 * @param stepper the amount of times the ant should update
	 */
	public void tick(int stepper) {
		for (int i = 0; i < stepper; i++) {
			mem = ant.updateAnt(mem, width, height, colors);
			ant.drawAnt(mem, width, height, colors);
			mem = ant2.updateAnt(mem, width, height, colors);
			ant2.drawAnt(mem, width, height, colors);
		}
	}

	/**
	 * Updates the ant once.
	 */
	private void tick() {
		tick(1);
	}

	/**
	 * Draws the image from memory to the screen.
	 *
	 * @param FPSs the property name
	 * @throws Exception if there is a graphics error.
	 */
	private void render(String FPSs, String UPSs) throws Exception {

		// setIgnoreRepaint(true);

		g = bs.getDrawGraphics();
		/*
		 * g.setColor(Color.GREEN); g.drawString(FPSs, width-60, 20);
		 * g.setColor(Color.RED); g.drawString(UPSs, width-67, 40);
		 */
		display.processImage(g, mem);

		g.setColor(Color.GREEN);
		g.drawString(FPSs, width - 82, 20);
		g.setColor(Color.RED);
		g.drawString(UPSs, width - 82, 40);

		bs.show();
		g.dispose();

	}

	/**
	 * Handles the timing of updating the ant.
	 */
	public class UpdateThread extends TickThread {

		/**
		 * UpdateThread constructor.
		 *
		 * @param tickRate sets the internal tick rate.
		 * @param name     the name of this thread.
		 */
		public UpdateThread(long tickRate, String name) {
			super(tickRate, name);
			setPaused(true);
		}

		public void run() {
			now = System.nanoTime();
			while (running) {

				if (!paused) {

					try {

						tick();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(601);
					}

					super.run();

					lapse += now - before;

					if (lapse >= 1_000_000_000L) {
						// System.out.println("UPS: " + ticks);
						lapse -= 1_000_000_000L;
						ticks = 0;
					}
					ticks++;

				} else {
					try {
						Thread.sleep(timePerTick / 1_000_000L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Handles the timing of drawing to the screen.
	 * 
	 * @param ut reference to the update thread.
	 */
	public class RenderThread extends TickThread {

		private UpdateThread ut;

		/**
		 * RenderThread constructor.
		 *
		 * @param tickRate sets the internal tick rate.
		 * @param name     the name of this thread.
		 * @param ut       reference to the update thread.
		 */
		public RenderThread(long tickRate, String name, UpdateThread ut) {
			super(tickRate, name);
			this.ut = ut;
		}

		public void run() {
			now = System.nanoTime();
			double lastFPS = 0, nowFPS = 0, lastUPS = 0, nowUPS = 0;
			while (running) {

				lastFPS = nowFPS;
				lastUPS = nowUPS;

				nowFPS = (double) (ticks) / ((double) (lapse) / 1_000_000_000.0);
				nowUPS = (double) (ut.ticks) / ((double) (lapse) / 1_000_000_000.0);

				try {
					if (!ut.isPaused())
						render(("FPS: " + String.format("%.1f", (nowFPS + lastFPS) / 2)),
								("UPS: " + String.format("%.1f", (nowUPS + lastUPS) / 2)));
					else
						render(("FPS: " + String.format("%.1f", (nowFPS + lastFPS) / 2)), ("UPS: paused"));
					/*
					 * bs.show(); g.dispose();
					 */

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(701);
				}

				super.run();

				lapse += now - before;

				if (lapse >= 1_000_000_000L) {
					// System.out.println("FPS: " + ticks);
					lapse -= 1_000_000_000L;
					ticks = 0;
				}
				ticks++;

			}
		}
	}

	// Game loop
	public void run() {

		init();

		updateThread = new UpdateThread(120L, "UpdateThread");
		renderThread = new RenderThread(60L, "RenderThread", updateThread);

		updateThread.start();
		renderThread.start();

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		stop();
	}

	public String getInstructionset() {
		return instructionset;
	}

	public Settings getSettings() {
		return new Settings(scale, spacing, antmargin);
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public int getAntmargin() {
		return antmargin;
	}

	public void setAntmargin(int antmargin) {
		this.antmargin = antmargin;
	}

	public void setInstructionset(String instructionset) {
		this.instructionset = instructionset;
	}

	public synchronized void clearMem() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Color pixel at x,y
				mem[x + y * width] = Color.BLACK.getRGB();
			}
		}
	}
}
