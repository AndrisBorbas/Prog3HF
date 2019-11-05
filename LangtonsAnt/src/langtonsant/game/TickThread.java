package langtonsant.game;

/**
 * A thread with a built-in tick rate based sleep system.
 */
public abstract class TickThread extends Thread {

	protected boolean running;

	// Update rate timer
	protected long timePerTick;

	// Time handling
	protected long now, before, timeTilNextFrame = 0L, millisTilNextFrame = 0L, lapse = 0L;

	// framerate and update rate counters
	protected int ticks = 0, nanosTilNextFrame = 0;

	/**
	 * TickThread constructor.
	 *
	 * @param tickRate sets the internal tick rate.
	 * @param name     the name of this thread.
	 */
	public TickThread(long tickRate, String name) {
		timePerTick = 1_000_000_000L / tickRate;
		before = System.nanoTime();
		this.setName(name);
		setRunning(true);
	}

	/**
	 * Sleeps for the necessary time to hit the <i>tickRate</i> if the operation in
	 * the tick did not last the whole tick.
	 */
	public void run() {
		before = now;
		now = System.nanoTime();

		timeTilNextFrame = timePerTick - (now - before);
		if (timeTilNextFrame > 0) {
			millisTilNextFrame = timeTilNextFrame / 1_000_000L;
			nanosTilNextFrame = (int) (timeTilNextFrame) % 1_000_000;
		} else {
			millisTilNextFrame = 0L;
			nanosTilNextFrame = 0;
			timeTilNextFrame = 0L;
		}

		try {
			Thread.sleep(millisTilNextFrame, nanosTilNextFrame);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		now = System.nanoTime();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
