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
public class SaltAndPepperDialog extends JDialog {

	public SaltAndPepperDialog(final Panel panel) {
		setTitle("Salt and pepper");
		setBounds(1, 1, 250, 150);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel paramPanel = new JPanel();
		paramPanel.setBorder(BorderFactory.createTitledBorder("Probabilities"));
		paramPanel.setBounds(0, 0, 250, 75);

		JLabel minLabel = new JLabel("Min = %");
		final JTextField minTextField = new JTextField("1");
		minTextField.setColumns(3);

		JLabel maxLabel = new JLabel("Max = %");
		final JTextField maxTextField = new JTextField("99");
		maxTextField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 75, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double minP = 0;
				double maxP = 0;
				try {
					minP = Double.valueOf(minTextField.getText());
					maxP = Double.valueOf(maxTextField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (minP > maxP || minP < 0 || minP > 100 || maxP < 0 || maxP > 100) {
					new MessageFrame("Invalid values");
					return;
				}
				Image image = panel.getImage();
				panel.setImage(NoiseUtils.saltAndPepper(image, minP * 0.01, maxP * 0.01));
				panel.repaint();
				dispose();
			}
		});

		paramPanel.add(minLabel);
		paramPanel.add(minTextField);

		paramPanel.add(maxLabel);
		paramPanel.add(maxTextField);

		this.add(paramPanel);
		this.add(okButton);
	}
	
}
