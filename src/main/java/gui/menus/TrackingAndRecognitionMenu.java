package gui.menus;

import gui.MessageFrame;
import gui.Panel;
import gui.Window;
import gui.tp3.HsvVideoTrackingDialog;
import gui.tp3.RgbImageTrackingDialog;
import gui.tp3.RgbVideoTrackingDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import application.Loader;
import application.utils.SiftUtils;
import domain.Image;

@SuppressWarnings("serial")
public class TrackingAndRecognitionMenu extends JMenu {

	public TrackingAndRecognitionMenu() {
		super("Tracking & Recognition");
		setEnabled(true);

		JMenuItem imageTracking = new JMenuItem("Image");
		imageTracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog trackingDialog = new RgbImageTrackingDialog(panel);
				trackingDialog.setVisible(true);
				
			}
		});
		
		JMenuItem rgbVideoTracking = new JMenuItem("RGB Video");
		rgbVideoTracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog trackingDialog = new RgbVideoTrackingDialog(panel);
				trackingDialog.setVisible(true);
				
			}
		});
		JMenuItem hvsVideoTracking = new JMenuItem("HVS Video");
		hvsVideoTracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog trackingDialog = new HsvVideoTrackingDialog(panel);
				trackingDialog.setVisible(true);
				
			}
		});
		JMenu sift = new JMenu("SIFT");
		
		JMenuItem siftAnalyze = new JMenuItem("Analyze");
		siftAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				Image a = panel.getImage();
				if (a == null) {
					return;
				}
				panel.setImage(SiftUtils.sift(a));
				panel.repaint();
			}
		});
		

		JMenuItem siftCompare = new JMenuItem("Compare");
		siftCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				Image a = panel.getImage();
				if (a == null) {
					return;
				}
				Image b = getSecondaryImage();
				Image[] pair = SiftUtils.sift(a, b);
				panel.setImage(pair[0]);
				panel.repaint();
				Window w = new Window();
				w.getPanel().setImage(pair[1]);
				w.setVisible(true);
			}
		});
		
		add(imageTracking);
		add(new JSeparator());
		add(rgbVideoTracking);
		add(hvsVideoTracking);
		add(new JSeparator());
		add(sift);
		sift.add(siftAnalyze);
		sift.add(siftCompare);
	}
	
	private Image getSecondaryImage() {
		Panel panel = (((Window) getTopLevelAncestor()).getPanel());
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(panel.fileFilter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(panel.fileFilter);
		chooser.showOpenDialog(TrackingAndRecognitionMenu.this);

		File arch = chooser.getSelectedFile();

		if (arch != null) {
			Image image = null;

			try {
				image = Loader.loadImage(arch);
			} catch (Exception ex) {
				ex.printStackTrace();
				new MessageFrame("Couldn't load the image");
			}

			return image;
		}

		return null;

	}
}
