package gui.tp1.punctual;

import gui.Panel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.utils.StatsUtilities;

@SuppressWarnings("serial")
public class GreyHistogramDialog extends JDialog {

	public GreyHistogramDialog(final Panel panel) {

		setTitle("Greyscale histogram");
		setBounds(1, 1, 600, 450);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		final BufferedImage histogram = StatsUtilities.getHistogram(panel
				.getImage());
		JPanel p1 = new JPanel();
		p1.setBounds(0, 0, 600, 400);
		p1.add(new JLabel(new ImageIcon(histogram)));
		JButton backButton = new JButton("Back");
		backButton.setSize(400, 30);
		backButton.setBounds(100, 400, 400, 30);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		add(p1);
		add(backButton);

	}
}
