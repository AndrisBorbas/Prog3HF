/**
 * 
 */
package langtonsant;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import langtonsant.entity.Heading;
import langtonsant.game.Game;

/**
 * @author A
 *
 */
public class Tests {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void GameBasicTest() throws IOException, InterruptedException {
		Game g = new Game("Langtons's Ant", 900, 900, 1, 0, 1, "RL", false);
		g.start();
		Thread.sleep(1000);
		Assert.assertEquals(new Settings(1, 0, 1), g.getSettings());
		Assert.assertEquals(true, g.updateThread.isPaused());
		Assert.assertEquals(true, g.updateThread.isRunning());
		Assert.assertEquals(900, g.display.getCanvas().getWidth());
	}

	@Test
	public void NewGameTest() throws IOException, AWTException {
		Game g = new Game("Langtons's Ant", 900, 900, 1, 0, 1, "RRLL", false);
		g.start();
		Robot robot = new Robot();
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_C);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_J);
		robot.keyRelease(KeyEvent.VK_J);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_K);
		robot.keyRelease(KeyEvent.VK_K);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_L);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_N);
		robot.delay(1000);
		for (int i = 0; i < 8; i++) {
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.delay(100);
		}
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(1000);
		Assert.assertEquals(new Settings(4, 0, 2), g.getSettings());
		Assert.assertEquals("RL", g.getInstructionset());
	}

	@Test
	public void OOBTest() throws Exception {
		Game g = new Game("Langtons's Ant", 900, 900, 1, 0, 1, "RL", false);
		g.start();
		Robot robot = new Robot();
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_L);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_L);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_L);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);
		robot.delay(1000);
	}

	@Test(expected = RuntimeException.class)
	public void errorTest() throws Exception {
		new Heading("WTF");
	}

	@Test
	public void ExportTest() throws IOException, AWTException {
		Game g = new Game("Langtons's Ant", 900, 900, 1, 0, 1, "RRLL", false);
		g.start();
		Robot robot = new Robot();
		robot.delay(2500);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_C);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_J);
		robot.keyRelease(KeyEvent.VK_J);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_K);
		robot.keyRelease(KeyEvent.VK_K);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_L);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_E);
		robot.keyRelease(KeyEvent.VK_E);
		robot.delay(1000);
		for (int i = 0; i < 2; i++) {
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.delay(100);
		}
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(1000);
		File f = new File("Game0.png");
		Assert.assertEquals(true, f.exists());
	}
}
