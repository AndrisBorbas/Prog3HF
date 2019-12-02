package langtonsant.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import langtonsant.Settings;

public class Setup extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3695678711472976064L;

	private Game game;

	private HashSet<Settings> settings;

	public Setup(String title, Game game) {
		super(title);
		this.setLayout(new GridBagLayout());
		this.game = game;

		settings = new HashSet<Settings>();

		loadSettings();

		addConfig(game.getSettings());
		
		saveSettings();

		createFrame();

	}

	private void createFrame() {
		/*
		 * try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
		 * catch (ClassNotFoundException | InstantiationException |
		 * IllegalAccessException | UnsupportedLookAndFeelException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;

		JComboBox settingsBox = new JComboBox(new Vector<Settings>(settings));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		this.add(settingsBox, c);

		settingsBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Settings selected = (Settings) ((JComboBox) e.getSource()).getSelectedItem();
				System.out.println(selected);
			}

		});

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
		
		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);

		for (Settings s : settings) {
			System.out.println(s);
		}

	}

	private void addConfig(Settings config) {
		settings.add(config);
	}

	private void loadSettings() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("settings.dat"));
			settings = (HashSet<Settings>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		for (Settings s : settings) {
			System.out.println(s);
		}
		System.out.println("in");

	}

	private void saveSettings() {
		try {
			//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("settings.dat"));
			FileOutputStream fos = new FileOutputStream("settings.xml");
			XMLEncoder encoder = new XMLEncoder(fos);
			
			encoder.setExceptionListener(new ExceptionListener() {
	            public void exceptionThrown(Exception e) {
	                System.out.println("Exception!: " + e.toString());
	            }
	        });
			
			encoder.writeObject(settings);
			/*
			for (Settings s : settings) {
				encoder.writeObject(s);
			}
			*/
			//oos.writeObject(settings);
			System.out.println("xml");
			encoder.close();
			fos.close();
			//oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
