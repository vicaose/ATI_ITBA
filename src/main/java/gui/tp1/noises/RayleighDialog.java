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
public class RayleighDialog extends JDialog {

	public RayleighDialog(final Panel panel) {
		setTitle("Exponential");
		setBounds(1, 1, 400, 150);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel paramPanel = new JPanel();
		paramPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		paramPanel.setBounds(0, 0, 400, 75);

		JLabel psiLabel = new JLabel("Psi = ");
		final JTextField psiTextField = new JTextField("1");
		psiTextField.setColumns(3);

		JLabel pLabel = new JLabel("Probability = ");
		final JTextField pTextField = new JTextField("50");
		pTextField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(50, 75, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double psi = 0;
				double p = 0;
				try {
					psi = Double.valueOf(psiTextField.getText());
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
				panel.setImage(NoiseUtils.rayleighNoise(image, psi, p*0.01));
				panel.repaint();
				dispose();
			}
		});

		paramPanel.add(psiLabel);
		paramPanel.add(psiTextField);

		paramPanel.add(pLabel);
		paramPanel.add(pTextField);

		this.add(paramPanel);
		this.add(okButton);
	}
	
}