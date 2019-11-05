package langtonsant.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class Display extends JFrame {

	private Canvas canvas;
	protected JMenuBar menuBar;
	private Game game;

	private int[] mem;

	private int width, height;

	public JMenuItem editMenuPause;

	public Display(String title, Game game, int width, int height, int[] mem) {
		super(title);
		this.width = width;
		this.height = height;
		this.game = game;
		this.mem = mem;

		createFrame();

	}

	private void createFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setAlwaysOnTop(false);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));

		BorderLayout layout = new BorderLayout();

		this.setLayout(layout);

		menuBar = new JMenuBar();

		crateMenuBar();

		this.setJMenuBar(menuBar);

		this.add(canvas, BorderLayout.CENTER);
		this.pack();
	}

	public boolean pause() {
		if (game.updateThread.isRunning()) {
			game.updateThread.setRunning(false);
			editMenuPause.setText("Resume");
			return false;
		} else {
			game.updateThread.setRunning(true);
			editMenuPause.setText("Pause");
			return true;
		}
	}

	private void crateMenuBar() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileMenuExport = new JMenuItem("Export");
		JMenuItem fileMenuExit = new JMenuItem("Exit");
		fileMenuExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame temp = new JFrame();
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);

				int returnVal = chooser.showSaveDialog(temp);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
					exportImage(chooser.getSelectedFile());
				}
			}
		});
		fileMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(fileMenuExport);
		fileMenu.add(fileMenuExit);
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		editMenuPause = new JMenuItem("Start");
		JMenuItem editMenuClear = new JMenuItem("Clear Screen");
		editMenuPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});
		editMenuClear.setToolTipText("This affects simulation as well");
		editMenuClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.clearMem();
			}
		});
		editMenu.add(editMenuPause);
		editMenu.add(editMenuClear);
		menuBar.add(editMenu);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuAbout = new JMenuItem("About");
		helpMenuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(helpMenu.getParent(), "Langton's Ant \n© Andris Borbás 2019", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(helpMenuAbout);
		menuBar.add(helpMenu);

	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void processImage(Graphics g, int[] mem) {
		setIgnoreRepaint(true);
		Image img = canvas.createImage(new MemoryImageSource(width, height, this.mem, 0, width));
		g.drawImage(img, 0, 0, width, height, null);
	}

	// public void paint (Graphics g) {}

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