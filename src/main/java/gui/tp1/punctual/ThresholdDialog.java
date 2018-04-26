package gui.tp1.punctual;

import gui.Panel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import application.utils.ThresholdUtils;
import domain.Image;

@SuppressWarnings("serial")
public class ThresholdDialog extends JDialog implements ChangeListener {

	private Panel panel;
	
	public ThresholdDialog(final Panel panel) {
		this.panel = panel;
		setTitle("Threshold");
		setBounds(1, 1, 500, 400);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan = new JPanel();
		pan.setBounds(0, 0, 200, 600);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panel.repaint();
			}
		});

		JSlider slider = new JSlider(JSlider.VERTICAL, 0, 255, 0);
		slider.addChangeListener(this);
		Dimension d = slider.getPreferredSize();  
		slider.setPreferredSize(new Dimension(d.width+50,d.height+150));
		slider.setBounds(0, 0, 100, 500);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		JButton okButton = new JButton("OK");
		okButton.setSize(250, 40);
		okButton.setBounds(200, 100, 250, 40);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setImage(panel.getTempImage());
                panel.repaint();
				dispose();
			}
		});

		pan.add(slider);

		this.add(pan);
		this.add(okButton);

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        Image modify = ThresholdUtils.threshold(panel.getImage(), (int)source.getValue());
			panel.setTempImage(modify);
			panel.repaint();
	    }

	}
}
