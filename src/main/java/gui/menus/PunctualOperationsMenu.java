package gui.menus;

import gui.Panel;
import gui.Window;
import gui.tp1.punctual.AddImagesItem;
import gui.tp1.punctual.ColorHistogramDialog;
import gui.tp1.punctual.ContrastDialog;
import gui.tp1.punctual.GreyHistogramDialog;
import gui.tp1.punctual.MultiplyImagesItem;
import gui.tp1.punctual.ScalarMultiplicationDialog;
import gui.tp1.punctual.SubstractImagesItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import application.utils.ImageConversionUtils;
import application.utils.PunctualOperationsUtils;
import domain.Image;
import domain.Image.ChannelType;

@SuppressWarnings("serial")
public class PunctualOperationsMenu extends JMenu {

	public PunctualOperationsMenu() {
		super("Punctual");
		setEnabled(true);

		JMenu algebraic = new JMenu("Algrebraic operations");

		JMenuItem addition = new AddImagesItem(this);
		JMenuItem substraction = new SubstractImagesItem(this);
		JMenuItem multiplication = new MultiplyImagesItem(this);

		JMenuItem convert = new JMenuItem("Format convertion");
		convert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				Image image = panel.getImage();
				if (image == null) {
					return;
				}
				if (image.isHsv()){
					System.out.println("Tengo una HSV");
				}else{
					System.out.println("Tengo una RGB");
				}
				panel.setImage(ImageConversionUtils.convert(image));
				if (panel.getImage().isHsv()) {
					System.out.println("La pase a HSV");
					System.out.println("h: "
							+ panel.getImage().getPixel(0, 0, ChannelType.HUE));
					System.out.println("s: "
							+ panel.getImage().getPixel(0, 0, ChannelType.SATURATION));
					System.out.println("v: "
							+ panel.getImage().getPixel(0, 0, ChannelType.VALUE));
				} else {
					System.out.println("La pase a RGB");
				}
				panel.repaint();
			}
		});
		JMenuItem scalarMultiplication = new JMenuItem("Scalar multiplication");
		scalarMultiplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog scalarMult = new ScalarMultiplicationDialog(panel);
				scalarMult.setVisible(true);
			}
		});
		JMenuItem dinamicRange = new JMenuItem("Dynamic range compression");
		dinamicRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				panel.setImage(PunctualOperationsUtils.dynamicCompression(panel
						.getImage()));
				panel.repaint();
			}
		});
		JMenuItem negative = new JMenuItem("Negative");
		negative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				panel.setImage(PunctualOperationsUtils.negative(panel
						.getImage()));
				panel.repaint();
			}
		});

		JMenuItem contrast = new JMenuItem("Contrast");
		contrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog contrastDialog = new ContrastDialog(panel);
				contrastDialog.setVisible(true);
			}
		});
		JMenuItem histogram = new JMenuItem("Histogram");
		histogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog histogramDialog;
				if (panel.getImage().getType() == Image.ImageType.GREYSCALE) {
					histogramDialog = new GreyHistogramDialog(panel);
				} else {
					histogramDialog = new ColorHistogramDialog(panel);
				}
				histogramDialog.setVisible(true);
			}
		});

		JMenuItem ecualization = new JMenuItem("Ecualization");
		ecualization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				panel.setImage(PunctualOperationsUtils.ecualizate(panel
						.getImage()));
				panel.repaint();
			}
		});

		JMenuItem toGreyscale = new JMenuItem("To Greyscale");
		toGreyscale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				panel.setImage(PunctualOperationsUtils.toGreyscale(panel
						.getImage()));
				panel.repaint();
			}
		});

		
		add(algebraic);
		algebraic.add(addition);
		algebraic.add(substraction);
		algebraic.add(multiplication);
		algebraic.add(scalarMultiplication);
		add(new JSeparator());
		add(dinamicRange);
		add(new JSeparator());
		add(negative);
		add(new JSeparator());
		add(contrast);
		add(new JSeparator());

		add(histogram);
		add(new JSeparator());
		add(ecualization);
		add(new JSeparator());
		add(toGreyscale);
		add(new JSeparator());
		add(convert);
	}

}
