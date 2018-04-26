package gui.tp1.filters;

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

import application.utils.MaskUtils;

@SuppressWarnings("serial")
public class MedianFilterDialog extends JDialog{
	
	public MedianFilterDialog(final Panel panel) {
		setTitle("Median filter");
		setBounds(1, 1, 250, 140);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder("Mask size"));
		pan.setBounds(0, 0, 250, 70);

		JLabel widthLabel = new JLabel("Width = ");
		final JTextField widthField = new JTextField("3");
		widthField.setColumns(3);

		JLabel heightLabel = new JLabel("Height = ");
		final JTextField heightField = new JTextField("3");
		heightField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 70, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int width, height;
				try {
					width = Integer.valueOf(widthField.getText());
					height = Integer.valueOf(heightField.getText());

				} catch (NumberFormatException ex) {
					new MessageFrame("Los datos ingresados son invalidos");
					return;
				}

				panel.setImage(MaskUtils.applyMedianMask(panel.getImage(), width, height));
				panel.repaint();
				dispose();

			}
		});

		pan.add(widthLabel);
		pan.add(widthField);

		pan.add(heightLabel);
		pan.add(heightField);

		this.add(pan);
		this.add(okButton);
	}

}
