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

import domain.Image;

@SuppressWarnings("serial")
public abstract class ImageCreatorDialog extends JDialog {

	public ImageCreatorDialog(final Panel panel) {
		setTitle("Create square image");
		setBounds(1, 1, 250, 130);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);
		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Size"));
		pan1.setBounds(0, 0, 250, 60);
		JLabel sizeLabel = new JLabel("Size = ");
		final JTextField sizeField = new JTextField("300");
		sizeField.setColumns(3);
		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 60, 250, 40);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int size;
				try {
					size = Integer.valueOf(sizeField.getText().trim());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid data");
					return;
				}
				if (size <= 0) {
					new MessageFrame("The image must be at least of 1x1");
					return;
				}
				Image img = createBinaryImage(size, size);
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
		pan1.add(sizeLabel);
		pan1.add(sizeField);
		this.add(pan1);
		this.add(okButton);
	}
	
	protected abstract Image createBinaryImage(int height, int width);
}
