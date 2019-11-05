package langtonsant.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

import javax.swing.*;

import langtonsant.entity.Ant;

public class Display {

	private JFrame frame;
	private Canvas canvas;
	protected JMenuBar menuBar;
	private Game game;

	private String title;
	private int width, height;

	public Display(String title, Game game, int width, int height, Ant ant) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.game = game;

		createFrame();

	}

	private void createFrame() {
		Color bgColor = new Color(34, 34, 34);
		Color fgColor = new Color(255, 255, 255);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UIManager.put("MenuBar.background", bgColor);
		UIManager.put("Menu.background", bgColor);
		UIManager.put("MenuItem.background", bgColor);
		UIManager.put("MenuBar.selectionBackground", bgColor);
		UIManager.put("Menu.selectionBackground", bgColor);
		UIManager.put("MenuItem.selectionBackground", bgColor);

		UIManager.put("MenuBar.foreground", fgColor);
		UIManager.put("Menu.foreground", fgColor);
		UIManager.put("MenuItem.foreground", fgColor);
		UIManager.put("MenuBar.selectionforeground", fgColor);
		UIManager.put("Menu.selectionforeground", fgColor);
		UIManager.put("MenuItem.selectionforeground", fgColor);

		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(false);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));

		BorderLayout layout = new BorderLayout();

		frame.setLayout(layout);

		menuBar = new JMenuBar();

		crateMenuBar();

		frame.setJMenuBar(menuBar);

		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
	}

	private void crateMenuBar() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileMenuExport = new JMenuItem("Export");
		JMenuItem fileMenuExit = new JMenuItem("Exit");
		fileMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(fileMenuExport);
		fileMenu.add(fileMenuExit);
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		JMenuItem editMenuPause = new JMenuItem("Pause");
		editMenuPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (game.updateThread.isRunning()) {
					game.updateThread.setRunning(false);
					editMenuPause.setText("Resume");
				} else {
					game.updateThread.setRunning(true);
					editMenuPause.setText("Pause");
				}
			}
		});
		editMenu.add(editMenuPause);
		menuBar.add(editMenu);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuAbout = new JMenuItem("About");
		helpMenuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Langton's Ant \n© Andris Borbás 2019", "About",
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
		Image img = canvas.createImage(new MemoryImageSource(width, height, mem, 0, width));
		g.drawImage(img, 0, 0, width, height, null);
	}

}