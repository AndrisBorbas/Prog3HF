package langtonsant.game;

public abstract class TickThread extends Thread {

	// update rate limiter
	protected double tickRate, timePerTick, tickDelta = 0;

	// delta timer
	protected long now, lastTime, frameCount = 0L, timer = 0L, timeTilNextFrame = 0L, millisTilNextFrame = 0L;

	// framerate and update rate counters
	protected int ticks = 0, frames = 0, nanosTilNextFrame = 0;

	// image
	protected int[] mem;

	public TickThread(double tickRate, int[] mem, String name) {
		this.tickRate = tickRate;
		timePerTick = 1000000000.0 / tickRate;
		this.mem = mem;
		lastTime = System.nanoTime();
		this.setName(name);
	}

	public void run() {
		lastTime = now;
		now = System.nanoTime();
		
		timeTilNextFrame = (long) (timePerTick - (now - lastTime));
		if (timeTilNextFrame > 0) {
			millisTilNextFrame = timeTilNextFrame / 1000000;
			nanosTilNextFrame = (int) (timeTilNextFrame % 1000000);
		} else {
			millisTilNextFrame = 0;
			nanosTilNextFrame = 0;
		}

		try {
			sleep(millisTilNextFrame, nanosTilNextFrame);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized int[] getMem() {
		return this.mem;
	}

	public synchronized void setMem(int[] mem) {
		this.mem = mem;
	}

}
