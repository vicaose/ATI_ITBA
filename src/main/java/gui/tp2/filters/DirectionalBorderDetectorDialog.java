package gui.tp2.filters;

import static domain.mask.MaskFactory.Direction.DIAGONAL;
import static domain.mask.MaskFactory.Direction.HORIZONTAL;
import static domain.mask.MaskFactory.Direction.INVERSE_DIAGONAL;
import static domain.mask.MaskFactory.Direction.VERTICAL;
import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import application.utils.MaskUtils;
import domain.SynthetizationType;
import domain.mask.Mask;
import domain.mask.MaskFactory.Direction;

@SuppressWarnings("serial")
public abstract class DirectionalBorderDetectorDialog extends JDialog {

	private Panel panel;
	private boolean diagonal = true;
	private boolean horizontal = true;
	private boolean vertical = true;
	private boolean inverseDiagonal = true;
	
	public DirectionalBorderDetectorDialog(final Panel panel, final String title) {
		setTitle(title);
		this.panel = panel;
		setBounds(1, 1, 450, 250);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel directionsPanel = new JPanel();
		directionsPanel.setBorder(BorderFactory
				.createTitledBorder("Directions:"));
		directionsPanel.setBounds(0, 0, 450, 80);
		JPanel synthetizationPanel = new JPanel();
		synthetizationPanel.setBorder(BorderFactory
				.createTitledBorder("Sinthetization:"));
		synthetizationPanel.setBounds(0, 80, 450, 80);

		final JCheckBox horizontalCheckBox = new JCheckBox("Horizontal", true);
		final JCheckBox verticalCheckBox = new JCheckBox("Vertical", true);
		final JCheckBox diagonalCheckBox = new JCheckBox("Diagonal", true);
		final JCheckBox otherDiagonalCheckBox = new JCheckBox("Inverse diagonal", true);

		final JRadioButton absRadioButton = new JRadioButton("Module", true);
		final JRadioButton maxRadioButton = new JRadioButton("Maximum");
		final JRadioButton minRadioButton = new JRadioButton("Minimum");
		final JRadioButton avgRadioButton = new JRadioButton("Average");

		maxRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxRadioButton.setSelected(true);
				minRadioButton.setSelected(!maxRadioButton.isSelected());
				avgRadioButton.setSelected(!maxRadioButton.isSelected());
				absRadioButton.setSelected(!maxRadioButton.isSelected());
			}
		});

		minRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				minRadioButton.setSelected(true);
				maxRadioButton.setSelected(!minRadioButton.isSelected());
				avgRadioButton.setSelected(!minRadioButton.isSelected());
				absRadioButton.setSelected(!minRadioButton.isSelected());
			}
		});

		avgRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				avgRadioButton.setSelected(true);
				maxRadioButton.setSelected(!avgRadioButton.isSelected());
				minRadioButton.setSelected(!avgRadioButton.isSelected());
				absRadioButton.setSelected(!avgRadioButton.isSelected());
			}
		});

		absRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				absRadioButton.setSelected(true);
				maxRadioButton.setSelected(!absRadioButton.isSelected());
				minRadioButton.setSelected(!absRadioButton.isSelected());
				avgRadioButton.setSelected(!absRadioButton.isSelected());
			}
		});
		horizontalCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DirectionalBorderDetectorDialog.this.horizontal = horizontalCheckBox.isSelected();
			}
		});
		otherDiagonalCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DirectionalBorderDetectorDialog.this.inverseDiagonal = otherDiagonalCheckBox.isSelected();
			}
		});
		diagonalCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DirectionalBorderDetectorDialog.this.diagonal = diagonalCheckBox.isSelected();
			}
		});
		verticalCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DirectionalBorderDetectorDialog.this.vertical = verticalCheckBox.isSelected();
			}
		});

		JButton okButton = new JButton("OK");
		okButton.setBounds(100, 180, 250, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SynthetizationType synthetizationType = null;
				if (!vertical && !horizontal && !diagonal && !inverseDiagonal) {
					new MessageFrame("Select a direction");
					return;
				}
				if (maxRadioButton.isSelected()) {
					synthetizationType = SynthetizationType.MAX;
				} else if (minRadioButton.isSelected()) {
					synthetizationType = SynthetizationType.MIN;
				} else if (avgRadioButton.isSelected()) {
					synthetizationType = SynthetizationType.AVG;
				} else if (absRadioButton.isSelected()) {
					synthetizationType = SynthetizationType.ABS;
				} else {
					throw new IllegalStateException();
				}

				DirectionalBorderDetectorDialog.this.applyFunction(synthetizationType);
			}
		});
		directionsPanel.add(horizontalCheckBox);
		directionsPanel.add(verticalCheckBox);
		directionsPanel.add(diagonalCheckBox);
		directionsPanel.add(otherDiagonalCheckBox);

		synthetizationPanel.add(absRadioButton);
		synthetizationPanel.add(maxRadioButton);
		synthetizationPanel.add(minRadioButton);
		synthetizationPanel.add(avgRadioButton);

		add(directionsPanel);
		add(synthetizationPanel);
		add(okButton);
	}

	protected abstract Mask getMask(Direction d);
	
	protected void applyFunction(SynthetizationType synthesizationType) {
		List<Mask> masks = new ArrayList<Mask>();
		if (horizontal) {
			masks.add(getMask(HORIZONTAL));
		}
		if (vertical) {
			masks.add(getMask(VERTICAL));
		}
		if (diagonal) {
			masks.add(getMask(DIAGONAL));
		}
		if (inverseDiagonal) {
			masks.add(getMask(INVERSE_DIAGONAL));
		}
		panel.setImage(MaskUtils.applyMasks(panel.getImage(), masks,
				synthesizationType));
		panel.repaint();
		dispose();
	}

}
