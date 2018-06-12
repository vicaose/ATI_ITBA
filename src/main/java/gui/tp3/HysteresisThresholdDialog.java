package gui.tp3;

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

@SuppressWarnings("serial")
public class HysteresisThresholdDialog extends JDialog {
	
	public HysteresisThresholdDialog(final Panel panel){
		setTitle("Hysteresis Threshold");
		setBounds(1, 1, 250, 220);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width/3 - getWidth()/3, size.height/3 - getHeight()/3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Lower threshold"));
		pan1.setBounds(0, 0, 250, 70);

		JPanel pan2 = new JPanel();
		pan2.setBorder(BorderFactory.createTitledBorder("Upper threshold"));
		pan2.setBounds(0, 70, 250, 70);

		JLabel coordLabel1 = new JLabel("Value =  ");
		final JTextField lowerThresholdTextField = new JTextField("");
		lowerThresholdTextField.setColumns(3);

		JLabel colorLabel = new JLabel("Value = ");
		final JTextField higherThresholdTextField = new JTextField("");
		higherThresholdTextField.setColumns(3);
		higherThresholdTextField.setAlignmentX(LEFT_ALIGNMENT);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 140, 250, 60);
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				double lowerThreshold = 0;
				double upperThreshold = 0;
				try{
					lowerThreshold = Double.valueOf(lowerThresholdTextField.getText());
					upperThreshold = Double.valueOf(higherThresholdTextField.getText());
					if (upperThreshold < lowerThreshold) {
						throw new NumberFormatException();
					}
				} catch(NumberFormatException ex){
					new MessageFrame("Invalid values");
					return;
				}
				panel.setImage(ThresholdUtils.hysteresis(panel.getImage(), lowerThreshold, upperThreshold));
				panel.repaint();
				dispose();
			}
		});

		pan1.add(coordLabel1);
		pan1.add(lowerThresholdTextField);

		pan2.add(colorLabel);
		pan2.add(higherThresholdTextField);

		this.add(pan1);
		this.add(pan2);
		this.add(okButton);

	};
}
