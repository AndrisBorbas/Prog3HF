package langtonsant.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import langtonsant.Main;
import langtonsant.Settings;

/**
 * Creates the window the set up a new simulation and saves settings for ease of
 * use
 * 
 * @author AndrisBorbas
 *
 */
public class Setup extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1621977115937294764L;

	/**
	 * Reference to the game
	 */
	private Game game;

	/**
	 * The list of the imported settings
	 */
	private HashSet<Settings> settings;

	/**
	 * The list of the imported instructionsets
	 */
	private TreeSet<String> instructions;

	/**
	 * The default and only constructor
	 * 
	 * @param title
	 * @param game
	 */
	public Setup(String title, Game game) {
		super(title);
		this.setLayout(new GridBagLayout());
		this.game = game;

		settings = new HashSet<Settings>();

		instructions = new TreeSet<String>();

		loadSettings();

		addConfig(game.getSettings());

		instructions.add(game.getInstructionset());

		saveSettings();

		createFrame();

	}

	/**
	 * Creates and fills the window with stuff
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
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;

		JComboBox<Settings> settingsBox = new JComboBox<Settings>(new Vector<Settings>(settings));
		settingsBox.setPreferredSize(new Dimension(390, 30));
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 0, 5);
		c.gridwidth = 3;
		this.add(settingsBox, c);

		SpinnerNumberModel scaleModel = new SpinnerNumberModel(3, 1, 10, 1);
		JSpinner scaleSpinner = new JSpinner(scaleModel);
		scaleSpinner.setPreferredSize(new Dimension(130, 24));
		SpinnerNumberModel spacingModel = new SpinnerNumberModel(1, 0, 1, 1);
		JSpinner spacingSpinner = new JSpinner(spacingModel);
		spacingSpinner.setPreferredSize(new Dimension(130, 24));
		SpinnerNumberModel antmarginModel = new SpinnerNumberModel(1, 1, 10, 1);
		JSpinner antmarginSpinner = new JSpinner(antmarginModel);
		antmarginSpinner.setPreferredSize(new Dimension(130, 24));

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(scaleSpinner, c);
		c.gridy = 1;
		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 0);
		this.add(spacingSpinner, c);
		c.gridy = 1;
		c.gridx = 2;
		c.insets = new Insets(0, 0, 0, 5);
		this.add(antmarginSpinner, c);

		JComboBox<String> instructionsBox = new JComboBox<String>(new Vector<String>(instructions));
		instructionsBox.setPreferredSize(new Dimension(390, 30));
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.insets = new Insets(5, 5, 0, 5);
		this.add(instructionsBox, c);

		JTextField instructionsText = new JTextField("RL");
		instructionsText.setPreferredSize(new Dimension(390, 30));
		c.gridy = 4;
		c.insets = new Insets(0, 5, 5, 5);
		this.add(instructionsText, c);

		JCheckBox multiAntCheck = new JCheckBox("IsMultiAnt");
		multiAntCheck.setHorizontalAlignment(SwingConstants.CENTER);
		c.gridy = 5;
		c.gridx = 1;
		c.gridwidth = 1;
		this.add(multiAntCheck, c);

		JButton startButton = new JButton("Start");
		c.gridy = 6;
		this.add(startButton, c);

		JButton startnExportButton = new JButton("Start & Export");
		c.gridy = 7;
		c.gridx = 0;
		c.gridwidth = 3;
		c.insets = new Insets(5, 5, 10, 5);
		this.add(startnExportButton, c);

		Settings selected = (Settings) settingsBox.getSelectedItem();
		scaleSpinner.setValue(selected.getScale());
		spacingSpinner.setValue(selected.getSpacing());
		antmarginSpinner.setValue(selected.getAntmargin());

		settingsBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Settings selected = (Settings) ((JComboBox<?>) e.getSource()).getSelectedItem();
				scaleSpinner.setValue(selected.getScale());
				spacingSpinner.setValue(selected.getSpacing());
				antmarginSpinner.setValue(selected.getAntmargin());
				System.out.println("Selected: " + selected);
			}

		});

		String selectedText = (String) instructionsBox.getSelectedItem();
		instructionsText.setText(selectedText);

		instructionsBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedText = (String) instructionsBox.getSelectedItem();
				instructionsText.setText(selectedText);
			}

		});

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (((int) antmarginSpinner.getValue()) > ((int) scaleSpinner.getValue())) {
					JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(),
							"Antmargin can't be more than Scale!", "About", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (!(instructionsText.getText().matches("^[RLNUrlnu]+$"))) {
					JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(),
							"Instructionset can only contain 'R', 'L', 'N' and 'U'!", "Warning",
							JOptionPane.WARNING_MESSAGE);

					return;
				}

				startNewGame(scaleSpinner, spacingSpinner, antmarginSpinner, instructionsText, multiAntCheck);
			}

		});

		startnExportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (((int) antmarginSpinner.getValue()) > ((int) scaleSpinner.getValue())) {
					JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(),
							"Antmargin can't be more than Scale!", "Warning", JOptionPane.WARNING_MESSAGE);

					return;
				}

				if (!(instructionsText.getText().matches("^[RLNUrlnu]+$"))) {
					JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(),
							"Instructionset can only contain 'R', 'L', 'N' and 'U'!", "Warning",
							JOptionPane.WARNING_MESSAGE);

					return;
				}

				game.display.exportImage(new File(game.thread.getName() + ".png"));

				startNewGame(scaleSpinner, spacingSpinner, antmarginSpinner, instructionsText, multiAntCheck);
			}

		});

		System.out.println(settingsBox.getMinimumSize());

		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);

	}

	/**
	 * Adds a config to the list
	 * 
	 * @param config
	 */
	private void addConfig(Settings config) {
		settings.add(config);
	}

	/**
	 * Loads the settings and instructionsets from file
	 */
	@SuppressWarnings("unchecked")
	private void loadSettings() {
		try {
			FileInputStream fis = new FileInputStream("settings.xml");
			XMLDecoder decoder = new XMLDecoder(fis);

			settings = (HashSet<Settings>) decoder.readObject();
			instructions = (TreeSet<String>) decoder.readObject();

			decoder.close();
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		for (Settings s : settings) {
			System.out.println(s);
		}

	}

	/**
	 * Saves the settings and instructionsets to file
	 */
	private void saveSettings() {
		try {
			FileOutputStream fos = new FileOutputStream("settings.xml");
			XMLEncoder encoder = new XMLEncoder(fos);

			encoder.setExceptionListener(new ExceptionListener() {
				public void exceptionThrown(Exception e) {
					System.out.println("Exception!: " + e.toString());
				}
			});

			encoder.writeObject(settings);
			encoder.writeObject(instructions);

			encoder.close();
			fos.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Starts a new simulation instance
	 * 
	 * @param scaleSpinner
	 * @param spacingSpinner
	 * @param antmarginSpinner
	 * @param instructionsText
	 */
	public void startNewGame(JSpinner scaleSpinner, JSpinner spacingSpinner, JSpinner antmarginSpinner,
			JTextField instructionsText, JCheckBox isMultiAnt) {

		game.setInstructionset(instructionsText.getText());
		game.setScale((int) scaleSpinner.getValue());
		game.setSpacing((int) spacingSpinner.getValue());
		game.setAntmargin((int) antmarginSpinner.getValue());
		game.setMultiAnt(isMultiAnt.isSelected());

		game.thread.interrupt();
		game.updateThread.setRunning(false);
		game.renderThread.setRunning(false);

		game.start();
		dispose();
	}
}
