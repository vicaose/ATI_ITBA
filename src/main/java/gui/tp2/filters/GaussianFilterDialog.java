package gui.tp2.filters;

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
public class GaussianFilterDialog extends JDialog {

	public GaussianFilterDialog(final Panel panel){
		setTitle("Gaussian filter");
		setBounds(1, 1, 250, 200);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width/3 - getWidth()/3, size.height/3 - getHeight()/3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBorder(BorderFactory.createTitledBorder("Mask size"));
		pan1.setBounds(0, 0, 250, 60);
		
		JPanel pan2 = new JPanel();
		pan2.setBorder(BorderFactory.createTitledBorder("Parameters"));
		pan2.setBounds(0, 70, 250, 60);

		JLabel coordLabel1 = new JLabel("Size ");
		final JTextField coordX = new JTextField("3");
		coordX.setColumns(3);
		
		JLabel sigmaPanel = new JLabel("Sigma ");
		final JTextField sigmaField = new JTextField("");
		sigmaField.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(0, 130, 250, 40);
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int size;
				double sigma;
				try{
					size = Integer.valueOf(coordX.getText());
					sigma = Double.valueOf(sigmaField.getText());

				} catch(NumberFormatException ex){
					new MessageFrame("Invalid values");
					return;
				}
				if (size % 2 == 0) {
					new MessageFrame("Invalid values");
					return;
				}
				panel.setImage(MaskUtils.applyMask(panel.getImage(), MaskFactory.buildGaussianMask(size, sigma)));
				panel.repaint();		
				dispose();

			}
		});

		pan1.add(coordLabel1);
		pan1.add(coordX);

		pan2.add(sigmaPanel);
		pan2.add(sigmaField);

		add(pan1);
		add(pan2);
		add(okButton);
	}
}
