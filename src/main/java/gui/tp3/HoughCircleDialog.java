package gui.tp3;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.utils.HoughUtils;
import domain.Image;

@SuppressWarnings("serial")
public class HoughCircleDialog extends JDialog {
	

	public HoughCircleDialog(final Panel panel) {

		setTitle("Hough Circle");
		setBounds(1, 1, 300, 200);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBounds(25, 0, 250, 100);

		JLabel epsLabel = new JLabel("Epsilon = ");
		final JTextField epsTextField = new JTextField("15");
		epsTextField.setColumns(5);
		
		JLabel thresLabel = new JLabel("Threshold = ");
		final JTextField thresTextField = new JTextField("0.9");
		thresTextField.setColumns(5);
		
		JLabel minRLabel = new JLabel("Min R = ");
		final JTextField minRTextField = new JTextField("10");
		minRTextField.setColumns(5);
		
		JLabel maxRLabel = new JLabel("Max R = ");
		final JTextField maxRTextField = new JTextField("20");
		maxRTextField.setColumns(5);
		

		JButton okButton = new JButton("OK");
		okButton.setSize(300, 40);
		okButton.setBounds(0, 100, 300, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double eps = 0.5;
				double thres = 0.9;
				int minR = 10;
				int maxR = 100;
				try {
					eps = Double.valueOf(epsTextField.getText());
					thres = Double.valueOf(thresTextField.getText());
					minR = Integer.valueOf(minRTextField.getText());
					maxR = Integer.valueOf(maxRTextField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (eps < 0 || thres < 0 || thres > 1 || minR < 0 || maxR < minR) {
					new MessageFrame("Invalid values");
					return;
				}
				Image houghed = HoughUtils.houghCircleDetector(panel.getImage(), eps, thres, minR, maxR);
				panel.setImage(houghed);
				panel.repaint();
				dispose();
			}
		});

		pan.add(epsLabel);
		pan.add(epsTextField);
		pan.add(thresLabel);
		pan.add(thresTextField);
		pan.add(minRLabel);
		pan.add(minRTextField);
		pan.add(maxRLabel);
		pan.add(maxRTextField);

		this.add(pan);
		this.add(okButton);

	}
	

}
