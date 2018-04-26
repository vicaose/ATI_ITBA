package gui.tp1.noises;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.utils.NoiseUtils;
import domain.Image;

@SuppressWarnings("serial")
public class GaussianDialog extends JDialog {

	public GaussianDialog(final Panel panel) {
		setTitle("Gaussian");
		setBounds(0, 0, 350, 200);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel paramPanel = new JPanel();
		paramPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		paramPanel.setBounds(0, 0, 350, 120);

		JLabel avgLabel = new JLabel("Average = ");
		final JTextField avgTextField = new JTextField("0");
		avgTextField.setColumns(3);

		JLabel devLabel = new JLabel("Deviation = ");
		final JTextField devTextField = new JTextField("1");
		devTextField.setColumns(3);

		JLabel pLabel = new JLabel("Probability = %");
		final JTextField pTextField = new JTextField("50");
		pTextField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(300, 40);
		okButton.setBounds(0, 120, 350, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double avg = 0;
				double dev = 1;
				double p = 20;
				try {
					avg = Double.valueOf(avgTextField.getText());
					dev = Double.valueOf(devTextField.getText());
					p = Double.valueOf(pTextField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (p < 0 || p > 100) {
					new MessageFrame("Invalid values");
					return;
				}
				Image image = panel.getImage();
				panel.setImage(NoiseUtils.gaussianNoise(image, avg, dev,
						p * 0.01));
				panel.repaint();
				dispose();
			}
		});

		paramPanel.add(avgLabel);
		paramPanel.add(avgTextField);

		paramPanel.add(devLabel);
		paramPanel.add(devTextField);

		paramPanel.add(pLabel);
		paramPanel.add(pTextField);

		this.add(paramPanel);
		this.add(okButton);
	}

}
