package langtonsant;

import langtonsant.Display;

public class Game implements Runnable {

	private Display display;
	public int width, height;
	public String title;

	private boolean running = false;
	private Thread thread;

	/*
	 * private BufferStrategy bs; private Graphics g;
	 */
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	// Initialization
	private void init() {
		display = new Display(title, width, height);
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
	private void tick() {

	}

	// Render tick
	private void render() {
		/*
		 * bs = display.getCanvas().getBufferStrategy(); if (bs == null) {
		 * display.getCanvas().createBufferStrategy(2); return; } g =
		 * bs.getDrawGraphics();
		 * 
		 * g.setColor(Color.GREEN); g.fill3DRect(100, 100, 200, 200, true);
		 * 
		 * bs.show(); g.dispose();
		 */
		display.drawboard.redraw();
	}

	// Game loop
	public void run() {

		init();

		double fps = 60;
		double timePerTick = 1000000000.0 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();

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

				tick();

				render();

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
