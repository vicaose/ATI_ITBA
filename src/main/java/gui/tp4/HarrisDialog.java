package gui.tp4;

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

import domain.mask.MaskFactory;

import application.utils.BorderDetectionUtils;

@SuppressWarnings("serial")
public class HarrisDialog extends JDialog {

	public HarrisDialog(final Panel panel) {
		setTitle("Harris");
		setBounds(1, 1, 350, 160);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Detect corners"));
		pan1.setBounds(0, 0, 350, 90);

		JLabel maskSizeLabel = new JLabel("Mask Size ");
		final JTextField maskSizeField = new JTextField("3");
		maskSizeField.setColumns(3);

		JLabel sigmaLabel = new JLabel("Sigma");
		final JTextField sigmaField = new JTextField("0.7");
		sigmaField.setColumns(3);

		JLabel kLabel = new JLabel("k");
		final JTextField kField = new JTextField("0.06");
		kField.setColumns(3);

		JLabel thresholdLabel = new JLabel("Threshold");
		final JTextField thresholdField = new JTextField("70");
		thresholdField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 90, 350, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size;
				double sigma, threshold, k;
				try {
					size = Integer.parseInt(maskSizeField.getText());
					sigma = Double.valueOf(sigmaField.getText());
					threshold = Double.valueOf(thresholdField.getText());
					k = Double.valueOf(kField.getText());
				} catch (NumberFormatException x) {
					new MessageFrame("Invalid values");
					return;
				}
				panel.setImage(BorderDetectionUtils.harris(panel.getImage(),
						MaskFactory.buildGaussianMask(1 + 2 * size, sigma),
						threshold, k));
				panel.repaint();
				dispose();
			}
		});

		pan1.add(maskSizeLabel);
		pan1.add(maskSizeField);

		pan1.add(sigmaLabel);
		pan1.add(sigmaField);

		pan1.add(kLabel);
		pan1.add(kField);

		pan1.add(thresholdLabel);
		pan1.add(thresholdField);

		this.add(pan1);
		this.add(okButton);

	};

}
