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
public class LogDialog extends JDialog {

	private boolean zeroCross = true;
	
	public LogDialog(final Panel panel) {
		setTitle("Log filter");

		setBounds(1, 1, 250, 250);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Mask size"));
		pan1.setBounds(0, 0, 250, 60);

		JPanel pan2 = new JPanel();
		pan2.setBorder(BorderFactory.createTitledBorder("Parameters"));
		pan2.setBounds(0, 70, 250, 110);

		JLabel coordLabel1 = new JLabel("Size ");
		final JTextField coordX = new JTextField("7");
		coordX.setColumns(3);

		JLabel sigmaPanel = new JLabel("Sigma ");
		final JTextField sigmaField = new JTextField("1");
		sigmaField.setColumns(3);
		
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
		okButton.setBounds(0, 180, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size;
				double sigma;
				int threshold;
				try {
					size = Integer.valueOf(coordX.getText());
					sigma = Double.valueOf(sigmaField.getText());
					threshold = Integer.valueOf(thresholdField.getText());

				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}
				if (size % 2 == 0) {
					new MessageFrame("Invalid values");
					return;
				}
				if (size < 2 * sigma) {
					new MessageFrame("Mask size too small");
					return;
				}
				if (zeroCross){
					panel.setImage(MaskUtils.applyMaskAndZeroCross(
							panel.getImage(), MaskFactory.buildLogMask(size, sigma), threshold));
				} else {
					panel.setImage(MaskUtils.applyMask(
							panel.getImage(), MaskFactory.buildLogMask(size, sigma)));
				}
				panel.repaint();
				dispose();

			}
		});

		pan1.add(coordLabel1);
		pan1.add(coordX);
		pan2.add(sigmaPanel);
		pan2.add(sigmaField);
		pan2.add(thresholdPanel);
		pan2.add(thresholdField);
		pan2.add(zeroCrossCheckBox);
		add(pan1);
		add(pan2);
		add(okButton);
	}
}
