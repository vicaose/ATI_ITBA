package gui.tp1.punctual;

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

import application.utils.PunctualOperationsUtils;
import domain.Image;
import static application.utils.BasicImageUtils.invalidValue;

@SuppressWarnings("serial")
public class ContrastDialog extends JDialog {

	public ContrastDialog(final Panel panel) {
		setBounds(1, 1, 270, 230);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel panR = new JPanel();
		panR.setBorder(BorderFactory.createTitledBorder("Original values"));
		panR.setBounds(0, 0, 270, 60);
		JLabel r1Label = new JLabel("R1 = ");
		final JTextField r1Field = new JTextField("0");
		r1Field.setColumns(3);
		JLabel r2Label = new JLabel("R2 = ");
		final JTextField r2Field = new JTextField("0");
		r2Field.setColumns(3);

		JPanel panS = new JPanel();
		panS.setBorder(BorderFactory.createTitledBorder("New values"));
		panS.setBounds(0, 75, 270, 60);
		JLabel s1Label = new JLabel("S1 = ");
		final JTextField s1Field = new JTextField("0");
		s1Field.setColumns(3);
		JLabel s2Label = new JLabel("S2 = ");
		final JTextField s2Field = new JTextField("0");
		s2Field.setColumns(3);

		JButton okButton = new JButton("OK");
		okButton.setSize(270, 40);
		okButton.setBounds(0, 150, 270, 40);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r1, r2, s1, s2;
				try {
					r1 = Integer.valueOf(r1Field.getText().trim());
					r2 = Integer.valueOf(r2Field.getText().trim());
					s1 = Integer.valueOf(s1Field.getText().trim());
					s2 = Integer.valueOf(s2Field.getText().trim());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid data");
					return;
				}
				if (invalidValue(r1) || invalidValue(r2) || invalidValue(s1) || invalidValue(s2)) {
					new MessageFrame("Invalid Parameters");
					return;
				}
				if(!(s1 < r1) || !(r2 < s2)){
					new MessageFrame("s1 must be less than r1, and r2 less than s2");
					return;
				}
				Image original = panel.getImage();
				panel.setImage(PunctualOperationsUtils.contrast(original, r1, r2, s1, s2));
				panel.repaint();
				dispose();
			}
		});
		panR.add(r1Label);
		panR.add(r1Field);

		panR.add(r2Label);
		panR.add(r2Field);

		panS.add(s1Label);
		panS.add(s1Field);

		panS.add(s2Label);
		panS.add(s2Field);
		add(panR);
		add(panS);
		add(okButton);
	}
}
