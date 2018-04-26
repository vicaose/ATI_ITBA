package gui.tp2.filters;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.utils.MaskUtils;
import domain.mask.MaskFactory;

@SuppressWarnings("serial")
public class LaplacianDialog extends JDialog {

	private boolean zeroCross = true;
	
	public LaplacianDialog(final Panel panel) {
		setTitle("Laplacian filter");

		setBounds(1, 1, 250, 140);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder("Parameters"));
		pan.setBounds(0, 0, 250, 60);

		JLabel thresholdPanel = new JLabel("Threshold ");
		final JTextField thresholdField = new JTextField("50");
		thresholdField.setColumns(3);
		
		final JCheckBox zeroCrossCheckBox = new JCheckBox("Zero cross", true);
		
		zeroCrossCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				zeroCross = zeroCrossCheckBox.isSelected();
				thresholdField.setEnabled(zeroCross);
			}
		});
		
		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 70, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int threshold;
				try {
					threshold = Integer.valueOf(thresholdField.getText());

				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}
				if (zeroCross) {
					panel.setImage(MaskUtils.applyMaskAndZeroCross(
						panel.getImage(), MaskFactory.buildLaplacianMask(), threshold));
				} else {
					panel.setImage(MaskUtils.applyMask(panel.getImage(), MaskFactory.buildLaplacianMask()));
					
				}
				panel.repaint();
				dispose();

			}
		});

		pan.add(thresholdPanel);
		pan.add(thresholdField);
		pan.add(zeroCrossCheckBox);
		add(pan);
		add(okButton);	}
}
