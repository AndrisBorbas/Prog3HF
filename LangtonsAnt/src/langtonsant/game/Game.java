package langtonsant.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import langtonsant.Settings;
import langtonsant.entity.Ant;

/**
 * The simulation instance, contains the updater and the renderer. Also the
 * Ant(s) and the memory image. Also creates the window.
 * 
 * @author AndrisBorbas
 *
 */
public class Game implements Runnable {

	/**
	 * The window where the simulation is displayed
	 */
	public Display display;
	/**
	 * The width and height of the window
	 */
	public int width, height;
	/**
	 * The title of the window
	 */
	public String title;
	/**
	 * How many times was the simulation restarted this run
	 */
	private static int runs = 0;

	/**
	 * The framebuffer strategy
	 */
	protected BufferStrategy bs;
	/**
	 * The graphics class for rendering onto the screen
	 */
	protected Graphics g;

	/**
	 * The thread of the simulation
	 */
	public Thread thread;

	/**
	 * The thread which updates the ants position
	 */
	public UpdateThread updateThread;
	/**
	 * The thread which renders the ant(s) to the screen
	 */
	public RenderThread renderThread;

	/**
	 * Should the simulation run with 2 ants
	 */
	private boolean multiAnt;

	/**
	 * The ant
	 */
	private Ant ant;
	/**
	 * The second ant, if <i>multiAnt</i> is true
	 */
	private Ant ant2;
	/**
	 * The parameters of the ant
	 */
	private int scale, spacing, antmargin;
	/**
	 * The instructionset of the ant
	 */
	private String instructionset;
	/**
	 * The memory location of the image where the ants "draw"
	 */
	protected int[] mem;

	private static boolean once = true;

	/**
	 * The LUT for coloring the image and checking for instruction
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

	/**
	 * The only Constructor of the Game
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @param scale
	 * @param spacing
	 * @param antmargin
	 * @param instructionset
	 * @param multiAnt
	 * @throws IOException
	 */
	public Game(String title, int width, int height, int scale, int spacing, int antmargin, String instructionset,
			boolean multiAnt) throws IOException {
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.spacing = spacing;
		this.antmargin = antmargin;
		this.instructionset = instructionset;
		this.multiAnt = multiAnt;

		mem = new int[width * height];
	}

	/**
	 * (Re)Initializes the memory, ant(s) and the graphics with the framebuffer.
	 */
	private void init() {

		once = true;

		clearMem();

		ant = new Ant(width / 2, height / 2, scale, spacing, antmargin, instructionset);
		display = new Display(title, this, width, height, mem);

		ant2 = new Ant(width / 8 * 5 - 6, height / 2, scale, spacing, antmargin, instructionset);

		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(2);
			bs = display.getCanvas().getBufferStrategy();
		}

		updateThread = new UpdateThread(120L, "UpdateThread" + runs);
		renderThread = new RenderThread(60L, "RenderThread" + runs, updateThread);

	}

	/**
	 * Threaded start
	 */
	public synchronized void start() {
		thread = new Thread(this);
		thread.setName("Game" + (runs++));
		thread.start();
	}

	/**
	 * Threaded stop
	 */
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
		try {
			for (int i = 0; i < stepper; i++) {
				mem = ant.updateAnt(mem, width, height, colors);
				ant.drawAnt(mem, width, height, colors);
				if (multiAnt) {
					mem = ant2.updateAnt(mem, width, height, colors);
					ant2.drawAnt(mem, width, height, colors);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();
			if (once) {
				once = false;
				updateThread.setPaused(true);
				new Setup("New simulation", this);
			}
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
	 * @param FPSs the current <i>fps</i> converted to string
	 * @param UPSs the current <i>ups</i> converted to string
	 * @throws Exception if there is a graphics error.
	 */
	private void render(String FPSs, String UPSs) throws Exception {

		g = bs.getDrawGraphics();

		display.processImage(g, mem);

		g.setColor(Color.GREEN);
		g.drawString(FPSs, width - 82, 24);
		g.setColor(Color.RED);
		g.drawString(UPSs, width - 84, 44);

		bs.show();
		g.dispose();

	}

	/**
	 * Handles the timing of updating the ant.
	 */
	public class UpdateThread extends TickThread {

		// public int stepper = 1;

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

						tick(/* stepper */);
						// stepper = 1;

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

	/**
	 * The main game loop, starts rendering and starts and pauses updating, also
	 * waits for the render and update threads to exit.
	 */
	public void run() {

		init();

		updateThread.start();
		renderThread.start();

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(
					"This is fine, it gets interrupted to wake up from waiting for the other thread to finish");
		}

		stop();
	}

	/**
	 * Resets the memory image to black
	 */
	public synchronized void clearMem() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Color pixel at x,y
				mem[x + y * width] = Color.BLACK.getRGB();
			}
		}
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return the spacing
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * @param spacing the spacing to set
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * @return the antmargin
	 */
	public int getAntmargin() {
		return antmargin;
	}

	/**
	 * @param antmargin the antmargin to set
	 */
	public void setAntmargin(int antmargin) {
		this.antmargin = antmargin;
	}

	/**
	 * @return the instructionset
	 */
	public String getInstructionset() {
		return instructionset;
	}

	/**
	 * @param instructionset the instructionset to set
	 */
	public void setInstructionset(String instructionset) {
		this.instructionset = instructionset;
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return new Settings(scale, spacing, antmargin);
	}

	/**
	 * @return the multiAnt
	 */
	public boolean isMultiAnt() {
		return multiAnt;
	}

	/**
	 * @param multiAnt the multiAnt to set
	 */
	public void setMultiAnt(boolean multiAnt) {
		this.multiAnt = multiAnt;
	}
}
