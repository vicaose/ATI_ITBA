package gui.tp2.diffusion;

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
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import application.utils.BorderDetectorFunction;
import application.utils.DiffusionUtils;
import domain.Image;

public class AnisotropicDiffusionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public AnisotropicDiffusionDialog(final Panel panel){
		setTitle("Anisotropic diffusion");
		setBounds(1, 1, 250, 200);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width/3 - getWidth()/3, size.height/3 - getHeight()/3);
		this.setResizable(false);
		setLayout(null);

		JPanel parametersPanel = new JPanel();
		parametersPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		parametersPanel.setBounds(0, 0, 250, 60);

		JPanel borderDetectorPanel = new JPanel();
		borderDetectorPanel.setBorder(BorderFactory.createTitledBorder("Border Detector"));
		borderDetectorPanel.setBounds(0, 60, 250, 60);



		JLabel iterationsLabel = new JLabel("Iterations ");
		final JTextField iterationsTextField = new JTextField("100");
		iterationsTextField.setColumns(3);

		JLabel sigmaLabel = new JLabel("Sigma ");
		final JTextField sigmaTextField = new JTextField("10");
		sigmaTextField.setColumns(3);

		final JRadioButton leclercRadioButton = new JRadioButton("Leclerc", true);
		final JRadioButton lorentzRadioButton = new JRadioButton("Lorentz");

		leclercRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				leclercRadioButton.setSelected(true);
				lorentzRadioButton.setSelected(!leclercRadioButton.isSelected());
			}
		});

		lorentzRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lorentzRadioButton.setSelected(true);
				leclercRadioButton.setSelected(!lorentzRadioButton.isSelected());
			}
		});

		JButton okButton = new JButton("OK");
		okButton.setBounds(0, 130, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double sigma = 0;
				int iterations = 0;
				try{
					sigma = Double.valueOf(sigmaTextField.getText());
					iterations = Integer.valueOf(iterationsTextField.getText());

				} catch(NumberFormatException ex){
					new MessageFrame("Invalid parameters.");
					return;
				}
				BorderDetectorFunction borderDetector = null;
				if(leclercRadioButton.isSelected()) {
					borderDetector = new DiffusionUtils.LeclercDetector(sigma);
				} else if(lorentzRadioButton.isSelected()) {
					borderDetector = new DiffusionUtils.LorentzDetector(sigma);
				} else {
					throw new IllegalStateException();
				}

				Image image = panel.getImage();
				Image diffused = DiffusionUtils.anisotropicDiffusion(image, iterations, borderDetector);
				panel.setImage(diffused);
				panel.repaint();
				dispose();
			}
		});

		parametersPanel.add(iterationsLabel);
		parametersPanel.add(iterationsTextField);
		parametersPanel.add(sigmaLabel);
		parametersPanel.add(sigmaTextField);

		borderDetectorPanel.add(leclercRadioButton);
		borderDetectorPanel.add(lorentzRadioButton);


		this.add(parametersPanel);
		this.add(borderDetectorPanel);
		this.add(okButton);

	};

}