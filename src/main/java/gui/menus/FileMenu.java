package gui.menus;

import gui.ExtensionFilter;
import gui.MessageFrame;
import gui.Panel;
import gui.Window;
import gui.tp0.CircleImageDialog;
import gui.tp0.CropDialog;
import gui.tp0.DegradeDialog;
import gui.tp0.RawImageDialog;
import gui.tp0.SquareImageDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileFilter;

import org.apache.sanselan.ImageWriteException;

import application.Loader;
import application.Saver;
import domain.Image;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	
	public FileMenu() {
		super("File");
		JMenuItem saveImage = new JMenuItem("Save image");
		JMenuItem cropImage = new JMenuItem("Crop image");
		this.setEnabled(true);
		JMenuItem undo = new JMenuItem("Undo");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				panel.undo();
				panel.repaint();
			}
		});
		JMenuItem redo = new JMenuItem("Redo");
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				panel.redo();
				panel.repaint();
			}
		});
		JMenuItem newWindow = new JMenuItem("New window");
		newWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window window = new Window();
				window.setVisible(true);
			}
		});
		JMenuItem loadImage = new JMenuItem("Load image");
		loadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(panel.fileFilter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(panel.fileFilter);
				chooser.showOpenDialog(FileMenu.this);
				File file = chooser.getSelectedFile();
				if (file != null) {
					Image image = null;
					try {
						image = Loader.loadImage(file);
					} catch (Exception ex) {
                        ex.printStackTrace();
						new MessageFrame("Couldn't load the image");
					}
					if (image != null) {
						panel.loadImage(image);
						panel.repaint();
					}
				}
			}
		});
		JMenuItem loadRaw = new JMenuItem("Load raw image");
		loadRaw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileFilter type = new ExtensionFilter("Raw images",
						new String[] { ".raw", ".RAW" });
				chooser.addChoosableFileFilter(type);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(type);
				chooser.showOpenDialog(FileMenu.this);
				File file = chooser.getSelectedFile();
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (file != null) {
					JDialog rawParams = new RawImageDialog(panel, file);
					rawParams.setVisible(true);
				}
			}
		});

		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser selector = new JFileChooser();
				selector.setApproveButtonText("Save");
				selector.showSaveDialog(FileMenu.this);
				File file = selector.getSelectedFile();
				if (file != null) {
					Image image = (((Window) getTopLevelAncestor()).getPanel()
							.getImage());
					try {
						Saver.saveImage(file, image);
					} catch (ImageWriteException ex) {
						new MessageFrame("Couldn't save the image");
					} catch (IOException ex) {
						new MessageFrame("Couldn't save the image");
					}
				}
			}
		});

		cropImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				JDialog crop = new CropDialog(panel);
				crop.setVisible(true);
			}
		});
		
		JMenuItem binaryImage = new JMenuItem("Square image");
		binaryImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				JDialog binaryImage = new SquareImageDialog(panel);
				binaryImage.setVisible(true);
			}
		});
		JMenuItem circleBinaryImage = new JMenuItem("Circle image");
		circleBinaryImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				JDialog binaryImage = new CircleImageDialog(panel);
				binaryImage.setVisible(true);
			}
		});
		JMenuItem degradeBW = new JMenuItem("Grey degradee");
		degradeBW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				JDialog degrade = new DegradeDialog(panel, false);
				degrade.setVisible(true);
			}
		});
		JMenuItem degradeColor = new JMenuItem("Color degradee");
		degradeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				JDialog degrade = new DegradeDialog(panel, true);
				degrade.setVisible(true);
			}
		});
		
		JMenu synthetic = new JMenu("Synthetic images");
		
		add(newWindow);
		add(new JSeparator());
		add(loadImage);
		add(loadRaw);
		add(saveImage);
		add(new JSeparator());
		add(cropImage);
		add(new JSeparator());
		add(undo);
		add(redo);
		add(new JSeparator());
		add(synthetic);
		synthetic.add(binaryImage);
		synthetic.add(circleBinaryImage);
		synthetic.add(degradeBW);
		synthetic.add(degradeColor);
	}
}
