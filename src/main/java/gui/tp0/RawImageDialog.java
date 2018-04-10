package gui.tp0;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.Loader;
import domain.Image;

@SuppressWarnings("serial")
public class RawImageDialog extends JDialog {

	public RawImageDialog(final Panel panel, final File arch) {
		setTitle("Load raw image");
		setBounds(1, 1, 250, 120);
		// setLocation(560, 400);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBounds(0, 0, 250, 50);

		JLabel anchoLabel = new JLabel("Width = ");
		final JTextField ancho = new JTextField("0");
		ancho.setColumns(3);

		JLabel altoLabel = new JLabel("Height = ");
		final JTextField alto = new JTextField("0");
		alto.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 50, 250, 40);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int X, Y;
				Image image = null;
				try {
					X = Integer.valueOf(ancho.getText());
					Y = Integer.valueOf(alto.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid data");
					return;
				}
				if (X <= 0 || Y <= 0) {
					new MessageFrame("Size must be possitive");
					return;
				}

				try {
					image = Loader.loadRaw(arch, X, Y);
				} catch (IOException ex) {
					new MessageFrame("Couldn't load the image");
					return;
				} catch (ArrayIndexOutOfBoundsException ex) {
					new MessageFrame("Size is smaller than specified");
					return;
				}
				
				if (image != null) {
					panel.loadImage(image);
					panel.repaint();
				}
				dispose();
			}
		});
		pan1.add(anchoLabel);
		pan1.add(ancho);
		pan1.add(altoLabel);
		pan1.add(alto);
		add(pan1);
		add(okButton);
	}
}
