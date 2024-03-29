package langtonsant.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import langtonsant.Main;

/**
 * The main window where the simulation is shown
 * 
 * @author AndrisBorbas
 *
 */
public class Display extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The canvas where the image made by the ants is draw to
	 */
	private Canvas canvas;
	/**
	 * The top menu bar for interaction with a mouse
	 */
	protected JMenuBar menuBar;
	/**
	 * Reference to the game
	 */
	private Game game;

	/**
	 * Reference to the memory image
	 */
	private int[] mem;

	/**
	 * The size of the window
	 */
	private int width, height;

	/**
	 * Reference to the pause button for setting its text
	 */
	public JMenuItem editMenuPause;

	/**
	 * The default and only constructor
	 */
	public Display(String title, Game game, int width, int height, int[] mem) {
		super(title);
		this.width = width;
		this.height = height;
		this.game = game;
		this.mem = mem;

		createFrame();

	}

	/**
	 * Creates the JFrame and fills it with stuff
	 */
	private void createFrame() {
		if (Main.isDarkMode) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(false);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));

		BorderLayout layout = new BorderLayout();

		this.setLayout(layout);

		menuBar = new JMenuBar();

		crateMenuBar();

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'n':
					game.updateThread.setPaused(true);
					new Setup("New simulation", game);
					break;
				case 'e':
					createTempExportFrame();
					break;
				case ' ':
					pause();
					break;
				case 'j':
					if (game.updateThread.isPaused())
						game.tick(1);
					break;
				case 'k':
					if (game.updateThread.isPaused())
						game.tick(100);
					break;
				case 'l':
					if (game.updateThread.isPaused())
						game.tick(10_000);
					break;
				case 'c':
					game.clearMem();
					break;
				case 27:
					System.exit(0);
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		// also add KeyListener to canvas and menubar:
		// https://stackoverflow.com/questions/286727/unresponsive-keylistener-for-jframe
		canvas.addKeyListener(this.getKeyListeners()[0]);
		menuBar.addKeyListener(this.getKeyListeners()[0]);

		this.setJMenuBar(menuBar);

		this.add(canvas, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Sets the Pausebuttons text and pauses the updating
	 * 
	 * @return the inverse of the state of the game
	 */
	public boolean pause() {
		if (game.updateThread.isPaused()) {
			game.updateThread.setPaused(false);
			editMenuPause.setText("Resume (space)");
			return false;
		} else {
			game.updateThread.setPaused(true);
			editMenuPause.setText("Pause (space)");
			return true;
		}
	}

	/**
	 * Creates the top menubar and fills it with stuff
	 */
	private void crateMenuBar() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileMenuNew = new JMenuItem("New (N)");
		JMenuItem fileMenuExport = new JMenuItem("Export (E)");
		JMenuItem fileMenuExit = new JMenuItem("Exit (Esc)");
		fileMenuNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				game.updateThread.setPaused(true);
				new Setup("New simulation", game);
			}

		});
		fileMenuExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createTempExportFrame();
			}
		});
		fileMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(fileMenuNew);
		fileMenu.add(fileMenuExport);
		fileMenu.add(fileMenuExit);
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		editMenuPause = new JMenuItem("Start (Space)");
		JMenuItem editMenuStep1 = new JMenuItem("Step 1 (J)");
		JMenuItem editMenuStep2 = new JMenuItem("Step 100 (K)");
		JMenuItem editMenuStep3 = new JMenuItem("Step 10000 (L)");
		JMenuItem editMenuClear = new JMenuItem("Clear Screen (C)");
		editMenuPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});

		editMenuStep1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.tick(1);
			}
		});
		editMenuStep2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.tick(100);
			}
		});
		editMenuStep3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.tick(10_000);
			}
		});

		editMenuClear.setToolTipText("This affects simulation as well");
		editMenuClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.clearMem();
			}
		});
		editMenu.add(editMenuPause);
		editMenu.add(editMenuStep1);
		editMenu.add(editMenuStep2);
		editMenu.add(editMenuStep3);
		editMenu.add(editMenuClear);
		menuBar.add(editMenu);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuAbout = new JMenuItem("About");
		helpMenuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(helpMenu.getParent(),
						"Langton's Ant \n� Andris Borb�s 2019\nhttp://github.andrisborbas.com/Prog3HF", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(helpMenuAbout);
		menuBar.add(helpMenu);

	}

	/**
	 * 
	 * @return the canvas
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * Converts the image to be displayed on the screen
	 * 
	 * @param g   the graphics class
	 * @param mem the memory image
	 */
	public void processImage(Graphics g, int[] mem) {
		setIgnoreRepaint(true);
		Image img = canvas.createImage(new MemoryImageSource(width, height, this.mem, 0, width));
		g.drawImage(img, 0, 0, width, height, null);
	}

	/**
	 * Exports the current state of the simulation to a png
	 * 
	 * @param file the file to export the image to
	 */
	public synchronized void exportImage(File file) {
		try {
			ImageIO.write(toBufferedImage(
					Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, this.mem, 0, width))),
					"png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates the file chooser window and gets the file
	 */
	public void createTempExportFrame() {
		JFrame temp = new JFrame();
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle("Specify a file to save");
		chooser.setSelectedFile(new File(game.thread.getName() + ".png"));

		int returnVal = chooser.showSaveDialog(temp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			if (!(f.getName().contains(".png"))) {
				f = new File(f.getName() + ".png");
			}
			System.out.println("You chose to save to: " + f.getName());
			exportImage(f);
		}
	}

	/**
	 * Converts to BufferedImage for export <a href=
	 * "https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage">
	 * https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage</a>
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}

}