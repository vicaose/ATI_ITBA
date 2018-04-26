package gui.menus;

import gui.Panel;
import gui.Window;
import gui.tp1.filters.MeanFilterDialog;
import gui.tp1.filters.MedianFilterDialog;
import gui.tp2.filters.GaussianFilterDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class FiltersMenu extends JMenu {

	public FiltersMenu() {
		super("Filter");
		setEnabled(true);

		JMenuItem gaussianFilter = new JMenuItem("Gaussian");
		gaussianFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog dialog = new GaussianFilterDialog(panel);
				dialog.setVisible(true);
			}
		});

		JMenuItem meanFilter = new JMenuItem("Mean");
		meanFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog dialog = new MeanFilterDialog(panel);
				dialog.setVisible(true);
			}
		});

		JMenuItem medianFilter = new JMenuItem("Median");
		medianFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Panel panel = (((Window) getTopLevelAncestor()).getPanel());
				if (panel.getImage() == null) {
					return;
				}
				JDialog dialog = new MedianFilterDialog(panel);
				dialog.setVisible(true);
			}
		});

		add(meanFilter);
		add(medianFilter);
		add(gaussianFilter);
	}
}
