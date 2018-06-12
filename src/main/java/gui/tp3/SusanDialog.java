package gui.tp3;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.utils.BorderDetectionUtils;
import domain.Image;

@SuppressWarnings("serial")
public class SusanDialog extends JDialog {

	public SusanDialog(final Panel panel) {

		setTitle("Susan");
		setBounds(1, 1, 300, 200);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBounds(0, 0, 300, 50);
		JPanel pan2 = new JPanel();
		pan2.setBounds(0, 50, 300, 50);

		JLabel minLabel = new JLabel("Min = ");
		final JTextField minTextField = new JTextField("0.375");
		minTextField.setColumns(5);
		JLabel maxLabel = new JLabel("Max = ");
		final JTextField maxTextField = new JTextField("0.625");
		maxTextField.setColumns(5);

		JLabel borderLabel = new JLabel("Borders: ");
		final JCheckBox borderDetectorCheckBox = new JCheckBox();

		JLabel cornerLabel = new JLabel("Corners: ");
		final JCheckBox cornerDetectorCheckBox = new JCheckBox();

		borderDetectorCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (borderDetectorCheckBox.isSelected()) {
					cornerDetectorCheckBox.setSelected(false);
					maxTextField.setText("0.625");
					minTextField.setText("0.375");
				}

			}
		});

		cornerDetectorCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cornerDetectorCheckBox.isSelected()) {
					borderDetectorCheckBox.setSelected(false);
					maxTextField.setText("0.875");
					minTextField.setText("0.625");
				}
			}
		});

		JButton okButton = new JButton("OK");
		okButton.setSize(300, 40);
		okButton.setBounds(0, 100, 300, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double min = 0.5;
				double max = 0.75;
				try {
					min = Double.valueOf(minTextField.getText());
					max = Double.valueOf(maxTextField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (min >= max || max >= 1 || min <= 0) {
					new MessageFrame("Invalid values");
					return;
				}
				Image susaned = BorderDetectionUtils.susan(panel.getImage(),
						min, max);
				panel.setImage(susaned);
				panel.repaint();
				dispose();
			}
		});

		pan.add(minLabel);
		pan.add(minTextField);
		pan.add(maxLabel);
		pan.add(maxTextField);
		pan2.add(borderLabel);
		pan2.add(borderDetectorCheckBox);
		pan2.add(cornerLabel);
		pan2.add(cornerDetectorCheckBox);

		this.add(pan);
		add(pan2);
		this.add(okButton);

	}

}
