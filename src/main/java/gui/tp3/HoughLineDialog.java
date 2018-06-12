package gui.tp3;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MessageFrame;
import gui.Panel;
import application.utils.HoughUtils;
import domain.Image;

@SuppressWarnings("serial")
public class HoughLineDialog extends JDialog {
		
		protected JPanel pan;

		public HoughLineDialog(final Panel panel) {

			setTitle("Hough Line");
			setBounds(1, 1, 300, 200);
			Dimension size = getToolkit().getScreenSize();
			setLocation(size.width / 3 - getWidth() / 3, size.height / 3
					- getHeight() / 3);
			this.setResizable(false);
			setLayout(null);

			pan = new JPanel();
			pan.setBounds(0, 0, 300, 100);

			JLabel epsLabel = new JLabel("Epsilon = ");
			final JTextField epsTextField = new JTextField("0.5");
			epsTextField.setColumns(5);
			
			JLabel thresLabel = new JLabel("Threshold = ");
			final JTextField thresTextField = new JTextField("0.9");
			thresTextField.setColumns(5);
			

			JButton okButton = new JButton("OK");
			okButton.setSize(300, 40);
			okButton.setBounds(0, 100, 300, 40);
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					double eps = 0.5;
					double thres = 0.9;
					try {
						eps = Double.valueOf(epsTextField.getText());
						thres = Double.valueOf(thresTextField.getText());
					} catch (NumberFormatException ex) {
						new MessageFrame("Invalid values");
						return;
					}

					if (eps < 0 || thres < 0 || thres > 1) {
						new MessageFrame("Invalid values");
						return;
					}
					Image houghed = HoughUtils.houghLineDetector(panel.getImage(), eps, thres);
					panel.setImage(houghed);
					panel.repaint();
					dispose();
				}
			});

			pan.add(epsLabel);
			pan.add(epsTextField);
			pan.add(thresLabel);
			pan.add(thresTextField);

			this.add(pan);
			this.add(okButton);

		}
		

}
