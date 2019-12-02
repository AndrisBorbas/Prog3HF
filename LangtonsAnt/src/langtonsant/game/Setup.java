package langtonsant.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import langtonsant.Settings;

public class Setup extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1621977115937294764L;

	private Game game;

	private HashSet<Settings> settings;

	private TreeSet<String> instructions;

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

	private void createFrame() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;

		JComboBox<Settings> settingsBox = new JComboBox<Settings>(new Vector<Settings>(settings));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		this.add(settingsBox, c);

		SpinnerNumberModel scaleModel = new SpinnerNumberModel(3, 1, 10, 1);
		JSpinner scaleSpinner = new JSpinner(scaleModel);
		SpinnerNumberModel spacingModel = new SpinnerNumberModel(1, 0, 1, 1);
		JSpinner spacingSpinner = new JSpinner(spacingModel);
		SpinnerNumberModel antmarginModel = new SpinnerNumberModel(1, 1, 10, 1);
		JSpinner antmarginSpinner = new JSpinner(antmarginModel);

		scaleSpinner.setMinimumSize(new Dimension(40, 16));
		spacingSpinner.setMinimumSize(new Dimension(40, 16));
		antmarginSpinner.setMinimumSize(new Dimension(40, 16));

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.weightx = 0.5;
		this.add(scaleSpinner, c);
		c.gridy = 1;
		c.gridx = 1;
		this.add(spacingSpinner, c);
		c.gridy = 1;
		c.gridx = 2;
		this.add(antmarginSpinner, c);

		JComboBox<String> instructionsBox = new JComboBox<String>(new Vector<String>(instructions));
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		this.add(instructionsBox, c);

		JTextField instructionsText = new JTextField("RL");
		c.gridy = 4;
		this.add(instructionsText, c);

		JButton startButton = new JButton("Start");
		c.gridy = 5;
		c.gridx = 1;
		c.gridwidth = 1;
		this.add(startButton, c);

		JButton startnExportButton = new JButton("Start & Export");
		c.gridy = 6;
		c.gridx = 0;
		c.gridwidth = 3;
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
				game.setInstructionset(instructionsText.getText());
				game.setScale((int) scaleSpinner.getValue());
				game.setSpacing((int) spacingSpinner.getValue());
				game.setAntmargin((int) antmarginSpinner.getValue());

				game.thread.interrupt();
				game.updateThread.setRunning(false);
				game.renderThread.setRunning(false);

				game.start();
				dispose();
			}

		});

		startnExportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				game.display.exportImage(new File(game.thread.getName() + ".png"));

				game.setInstructionset(instructionsText.getText());
				game.setScale((int) scaleSpinner.getValue());
				game.setSpacing((int) spacingSpinner.getValue());
				game.setAntmargin((int) antmarginSpinner.getValue());

				game.thread.interrupt();
				game.updateThread.setRunning(false);
				game.renderThread.setRunning(false);

				game.start();
				dispose();
			}

		});

		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);

	}

	private void addConfig(Settings config) {
		settings.add(config);
	}

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
}
