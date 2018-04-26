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
import domain.mask.MaskFactory;

@SuppressWarnings("serial")
public class EdgeEnhancementDialog extends JDialog {

	public EdgeEnhancementDialog(final Panel panel) {
		setTitle("Edge enhancement");
		setBounds(1, 1, 250, 140);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder("Mask size"));
		pan.setBounds(0, 0, 250, 70);

		JLabel sizeLabel = new JLabel("Size = ");
		final JTextField sizeField = new JTextField("3");
		sizeField.setColumns(3);

//		JLabel heightLabel = new JLabel("Height = ");
//		final JTextField heightField = new JTextField("3");
//		heightField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 70, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size;
				try {
					size = Integer.valueOf(sizeField.getText());
//					h = Integer.valueOf(heightField.getText());

				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}
				if (size % 2 == 0) {
					new MessageFrame("Invalid values");
					return;
				}
				panel.setImage(MaskUtils.applyMask(panel.getImage(), MaskFactory.buildEdgeEnhancementMask(size, size)));
				panel.repaint();
				dispose();

			}
		});

		pan.add(sizeLabel);
		pan.add(sizeField);

//		pan.add(heightLabel);
//		pan.add(heightField);

		add(pan);
		add(okButton);
	}
}
