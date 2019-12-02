package langtonsant.game;

/**
 * A thread with a built-in tick rate based sleep system.
 */
public abstract class TickThread extends Thread {

	protected boolean running = true, paused = false;

	/**
	 * Update rate timer
	 */
	protected long timePerTick;

	/**
	 * Time handling
	 */
	protected long now, before, timeTilNextFrame = 0L, millisTilNextFrame = 0L, lapse = 0L;

	/**
	 * framerate and update rate counters
	 */
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
		now = System.nanoTime();
		this.setName(name);
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

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
