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
import static domain.Image.ChannelType.*;

@SuppressWarnings("serial")
public class ColorHistogramDialog extends JDialog {

	public ColorHistogramDialog(Panel panel) {
		setTitle("Color histogram");
		setBounds(1, 1, 1000, 300);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		final BufferedImage redHistogram = StatsUtilities.getHistogram(
				panel.getImage(), RED);
		final BufferedImage greenHistogram = StatsUtilities.getHistogram(
				panel.getImage(), GREEN);
		final BufferedImage blueHistogram = StatsUtilities.getHistogram(
				panel.getImage(), BLUE);
		JPanel p1 = new JPanel();
		p1.setBounds(0, 0, 1000, 200);
		p1.add(new JLabel(new ImageIcon(redHistogram)));
		p1.add(new JLabel(new ImageIcon(greenHistogram)));
		p1.add(new JLabel(new ImageIcon(blueHistogram)));
		JButton backButton = new JButton("Back");
		backButton.setSize(400, 30);
		backButton.setBounds(300, 220, 400, 30);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		add(p1);
		add(backButton);
	}

}
