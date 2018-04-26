package gui.tp2.thresholds;

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

import application.utils.ThresholdUtils;
import domain.Image;
import static domain.Image.ChannelType.*;


@SuppressWarnings("serial")
public class ColorGlobalThresholdDialog extends JDialog {
	
	public ColorGlobalThresholdDialog(final Panel panel) {
		
		setTitle("Global Threshold");
		setBounds(1, 1, 250, 300);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel red = new JPanel();
		red.setBounds(0, 0, 250, 75);
		red.setBorder(BorderFactory.createTitledBorder("Red"));
		JLabel redTLabel = new JLabel("T = ");
		final JTextField redTTextField = new JTextField("128");
		redTTextField.setColumns(3);	
		JLabel redDeltaLabel = new JLabel("delta = ");
		final JTextField redDeltaTextField = new JTextField("1");
		redDeltaTextField.setColumns(3);
		
		JPanel green = new JPanel();
		green.setBounds(0, 75, 250, 75);
		green.setBorder(BorderFactory.createTitledBorder("Green"));
		JLabel greenTLabel = new JLabel("T = ");
		final JTextField greenTTextField = new JTextField("128");
		greenTTextField.setColumns(3);	
		JLabel greenDeltaLabel = new JLabel("delta = ");
		final JTextField greenDeltaTextField = new JTextField("1");
		greenDeltaTextField.setColumns(3);
		
		JPanel blue = new JPanel();
		blue.setBounds(0, 150, 250, 75);
		blue.setBorder(BorderFactory.createTitledBorder("Blue"));
		JLabel blueTLabel = new JLabel("T = ");
		final JTextField blueTTextField = new JTextField("128");
		blueTTextField.setColumns(3);	
		JLabel blueDeltaLabel = new JLabel("delta = ");
		final JTextField blueDeltaTextField = new JTextField("1");
		blueDeltaTextField.setColumns(3);
		
		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 225, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int redT = 128;
				int redDelta = 1;
				int greenT = 128;
				int greenDelta = 1;
				int blueT = 128;
				int blueDelta = 1;
				try {
					redT = Integer.valueOf(redTTextField.getText());
					greenDelta = Integer.valueOf(greenDeltaTextField.getText());
					greenT = Integer.valueOf(greenTTextField.getText());
					greenDelta = Integer.valueOf(greenDeltaTextField.getText());
					blueT = Integer.valueOf(blueTTextField.getText());
					blueDelta = Integer.valueOf(blueDeltaTextField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (invalidT(redT, redDelta) || invalidT(greenT, greenDelta) || invalidT(blueT, blueDelta)) {
					new MessageFrame("Invalid values");
					return;
				}
				Image image = panel.getImage();
				ThresholdUtils.global(image, RED, redT, redDelta);
				ThresholdUtils.global(image, GREEN, greenT, greenDelta);
				ThresholdUtils.global(image, BLUE, blueT, blueDelta);
				panel.setImage(image);
				panel.repaint();
				dispose();
			}
		});
		
		red.add(redTLabel);
		red.add(redTTextField);
		red.add(redDeltaLabel);
		red.add(redDeltaTextField);
		
		green.add(greenTLabel);
		green.add(greenTTextField);
		green.add(greenDeltaLabel);
		green.add(greenDeltaTextField);
		
		blue.add(blueTLabel);
		blue.add(blueTTextField);
		blue.add(blueDeltaLabel);
		blue.add(blueDeltaTextField);

		this.add(red);
		this.add(green);
		this.add(blue);
		
		this.add(okButton);
		
	}
	
	private boolean invalidT(int t, int delta) {
		return t > 255 || t < 0 || delta > 255 || delta < 1;
	}

}
