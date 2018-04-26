package gui.tp2.thresholds;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.utils.ThresholdUtils;
import domain.Image;

@SuppressWarnings("serial")
public class GlobalThresholdDialog extends JDialog {

	public GlobalThresholdDialog(final Panel panel) {

		setTitle("Global Threshold");
		setBounds(1, 1, 250, 150);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBounds(0, 0, 250, 75);

		JLabel tLabel = new JLabel("T = ");
		final JTextField tTextField = new JTextField("128");
		tTextField.setColumns(3);

		JLabel deltaLabel = new JLabel("delta = ");
		final JTextField deltaTextField = new JTextField("1");
		deltaTextField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 75, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int T = 128;
				int delta = 1;
				try {
					T = Integer.valueOf(tTextField.getText());
					delta = Integer.valueOf(deltaTextField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (T > 255 || T < 0 || delta > 255 || delta < 1) {
					new MessageFrame("Invalid values");
					return;
				}
				Image thresholded = ThresholdUtils.global(
						panel.getImage(), T, delta);
				panel.setImage(thresholded);
				panel.repaint();
				dispose();
			}
		});

		pan.add(tLabel);
		pan.add(tTextField);
		pan.add(deltaLabel);
		pan.add(deltaTextField);

		this.add(pan);
		this.add(okButton);

	}

}
