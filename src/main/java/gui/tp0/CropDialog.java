package gui.tp0;

import gui.MessageFrame;
import gui.Panel;

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
public class CropDialog extends JDialog {

	public CropDialog(final Panel panel) {
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
		pan2.setBorder(BorderFactory.createTitledBorder("Start Point"));
		pan2.setBounds(0, 60, 270, 60);

		JLabel startPointLabelX = new JLabel("X = ");
		final JTextField startPointFieldX = new JTextField("0");
		startPointFieldX.setColumns(3);
		JLabel startPointLabelY = new JLabel("Y = ");
		final JTextField startPointFieldY = new JTextField("0");
		startPointFieldY.setColumns(3);

		JLabel heightLabel = new JLabel("Height = ");
		final JTextField heightField = new JTextField("100");
		heightField.setColumns(3);

		JLabel widthLabel = new JLabel("Width = ");
		final JTextField widthField = new JTextField("100");
		widthField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(270, 40);
		okButton.setBounds(0, 120, 270, 40);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int height;
				int width;
				int x;
				int y;
				try {
					height = Integer.valueOf(heightField.getText().trim());
					width = Integer.valueOf(widthField.getText().trim());
					y = Integer.valueOf(startPointFieldX.getText().trim());
					x = Integer.valueOf(startPointFieldY.getText().trim());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid data");
					return;
				}

				Image original = panel.getImage();
				if (height <= 0 || width <= 0 || x < 0 || y < 0
						|| x >= original.getHeight()
						|| y >= original.getWidth()
						|| x + height >= original.getHeight()
						|| y + width > original.getWidth()) {
					new MessageFrame("Invalid Parameters");
					return;
				}
				Image img = BasicImageUtils.crop(height,width, x, y, original);
				panel.setImage(img);
				panel.repaint();
				dispose();
			}
		});
		pan1.add(heightLabel);
		pan1.add(heightField);
		pan1.add(widthLabel);
		pan1.add(widthField);
		pan2.add(startPointLabelX);
		pan2.add(startPointFieldX);
		pan2.add(startPointLabelY);
		pan2.add(startPointFieldY);
		add(pan1);
		add(pan2);
		add(okButton);
	}
}
