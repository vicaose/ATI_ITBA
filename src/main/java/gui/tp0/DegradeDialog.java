package gui.tp0;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.utils.BasicImageUtils;
import domain.Image;

@SuppressWarnings("serial")
public class DegradeDialog extends JDialog {

	public DegradeDialog(final Panel panel, final boolean isColor) {

		setBounds(1, 1, 270, 190);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Size"));
		pan1.setBounds(0, 0, 270, 60);

		JPanel pan2 = new JPanel();
		pan2.setBorder(BorderFactory.createTitledBorder("Color"));
		pan2.setBounds(0, 60, 270, 60);

		JLabel heightLabel = new JLabel("Height = ");
		final JTextField height = new JTextField("512");
		height.setColumns(3);

		JLabel widthLabel = new JLabel("Width = ");
		final JTextField width = new JTextField("512");
		width.setColumns(3);

		String fieldText = "0";
		if (isColor) {
			fieldText = "000000";
			setTitle("Create color degradee");
		} else {
			setTitle("Create grey degradee");
		}

		JLabel colorLabel1 = new JLabel("From:");
		final JTextField color1 = new JTextField(fieldText);
		color1.setColumns(5);

		JLabel colorLabel2 = new JLabel("To:");
		final JTextField color2 = new JTextField(fieldText);
		color2.setColumns(5);

		JButton okButton = new JButton("OK");
		okButton.setSize(270, 40);
		okButton.setBounds(0, 120, 270, 40);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int heightVal;
				int widthVal;
				int c1;
				int c2;

				try {
					heightVal = Integer.valueOf(height.getText().trim());
					widthVal = Integer.valueOf(width.getText().trim());
					if (isColor) {
						c1 = Integer.valueOf(color1.getText().trim(), 16);
						c2 = Integer.valueOf(color2.getText().trim(), 16);
					} else {
						c1 = Integer.valueOf(color1.getText().trim());
						c2 = Integer.valueOf(color2.getText().trim());
						c1 = new Color(c1, c1, c1).getRGB();
						c2 = new Color(c2, c2, c2).getRGB();
					}

				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid data");
					return;
				}

				if (heightVal <= 0 || widthVal <= 0) {
					new MessageFrame("The image must be at least of 1x1");
					return;
				}
				Image img = BasicImageUtils.createDegrade(isColor, heightVal, widthVal, c1,
						c2);
				if (img != null) {
					panel.setImage(img);
					panel.repaint();
					dispose();
				} else {
					new MessageFrame("Invalid values");
					return;
				}

			}
		});
		pan1.add(heightLabel);
		pan1.add(height);
		pan1.add(widthLabel);
		pan1.add(width);
		pan2.add(colorLabel1);
		pan2.add(color1);
		pan2.add(colorLabel2);
		pan2.add(color2);
		add(pan1);
		add(pan2);
		add(okButton);
	};
}
