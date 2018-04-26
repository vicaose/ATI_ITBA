package gui.menus;

import gui.Panel;
import gui.Window;
import gui.tp2.diffusion.AnisotropicDiffusionDialog;
import gui.tp2.diffusion.IsotropicDiffusionDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class DiffusionMenu extends JMenu {

	public DiffusionMenu() {
		super("Diffusion");
		setEnabled(true);
		
		JMenuItem isotropic = new JMenuItem("Isotropic");
		isotropic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog iso = new IsotropicDiffusionDialog(panel);
				iso.setVisible(true);
			}
		});
		
		JMenuItem anisotropic = new JMenuItem("Anisotropic");
		anisotropic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog ani = new AnisotropicDiffusionDialog(panel);
				ani.setVisible(true);
			}
		});
		
		add(isotropic);
		add(new JSeparator());
		add(anisotropic);
	}
		
}
