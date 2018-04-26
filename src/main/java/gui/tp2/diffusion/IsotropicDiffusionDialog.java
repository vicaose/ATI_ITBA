package gui.tp2.diffusion;

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

import application.utils.DiffusionUtils;
import domain.Image;

@SuppressWarnings("serial")
public class IsotropicDiffusionDialog extends JDialog {

	public IsotropicDiffusionDialog(final Panel panel) {
		setTitle("Isotropic Diffusion");
		setBounds(1, 1, 250, 120);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Parameters"));
		pan1.setBounds(0, 0, 250, 50);

		JLabel iterationsLabel = new JLabel("Iterations");
		final JTextField iterationsField = new JTextField("7");
		iterationsField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 50, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int iterations = 0;
				try {
					iterations = Integer.valueOf(iterationsField.getText());

				} catch (NumberFormatException ex) {
					new MessageFrame("Parameters are invalid.");
					return;
				}
				Image image = panel.getImage();
				Image diffused = DiffusionUtils.isotropicDiffusion(image, iterations);
				panel.setImage(diffused);
				panel.repaint();
				dispose();
			}
		});

		pan1.add(iterationsLabel);
		pan1.add(iterationsField);

		this.add(pan1);
		this.add(okButton);
	}
}
