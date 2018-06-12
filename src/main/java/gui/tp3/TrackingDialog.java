package gui.tp3;

import gui.MessageFrame;
import gui.Panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import listeners.DragAndDropListener;

@SuppressWarnings("serial")
public abstract class TrackingDialog extends JDialog {

	protected Panel panel;
	protected DragAndDropListener dragAndDropListener;

	public TrackingDialog(final Panel panel) {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panel = panel;
		setTitle("Video Tracking");
		setBounds(1, 1, 300, 250);
		Dimension size = getToolkit().getScreenSize();
		setLocation(size.width / 3 - getWidth() / 3 + panel.getWidth(),
				size.height / 3 - getHeight() / 3);
		this.setResizable(false);
		setLayout(null);

		JPanel pan1 = new JPanel();
		pan1.setBounds(0, 0, 300, 50);

		JPanel pan2 = new JPanel();
		pan2.setBounds(0, 50, 300, 50);
		JPanel pan3 = new JPanel();
		pan3.setBounds(0, 100, 300, 50);

		JLabel pLabel = new JLabel("(x0,y0): ");
		final JTextField pxTextField = new JTextField("0");
		pxTextField.setColumns(5);
		final JTextField pyTextField = new JTextField("0");
		pyTextField.setColumns(5);

		JLabel qLabel = new JLabel("(x1,y1): ");
		final JTextField qxTextField = new JTextField("0");
		qxTextField.setColumns(5);
		final JTextField qyTextField = new JTextField("0");
		qyTextField.setColumns(5);

		JLabel itLabel = new JLabel("iterations: ");
		final JTextField itField = new JTextField("20");

		
		JButton okButton = new JButton("OK");
		okButton.setSize(300, 40);
		okButton.setBounds(0, 150, 300, 40);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int px = 0;
				int py = 0;
				int qx = 0;
				int qy = 0;
				int iter = 0;
				try {
					px = Integer.valueOf(pxTextField.getText());
					py = Integer.valueOf(pyTextField.getText());
					qx = Integer.valueOf(qxTextField.getText());
					qy = Integer.valueOf(qyTextField.getText());
					iter = Integer.valueOf(itField.getText());
				} catch (NumberFormatException ex) {
					new MessageFrame("Invalid values");
					return;
				}

				if (px > qx) {
					int aux;
					aux = qx;
					qx = px;
					px = aux;
				}
				if (py > qy) {
					int aux;
					aux = qy;
					qy = py;
					py = aux;
				}
				if ( iter <=0 || px < 0 || py < 0 || qx > panel.getImage().getWidth()
						|| qy > panel.getImage().getHeight()) {
					new MessageFrame("Invalid values");
					return;
				}

				track(px, py, qx, qy, iter);
			}
		});

		pan1.add(pLabel);
		pan1.add(pxTextField);
		pan1.add(pyTextField);
		pan2.add(qLabel);
		pan2.add(qxTextField);
		pan2.add(qyTextField);
		pan3.add(itLabel);
		pan3.add(itField);

		dragAndDropListener = new DragAndDropListener(panel, pxTextField,
				pyTextField, qxTextField, qyTextField);
		panel.addMouseListener(dragAndDropListener);
		panel.addMouseMotionListener(dragAndDropListener);

		this.add(pan1);
		this.add(pan2);
		add(pan3);
		this.add(okButton);
	}

	@Override
	public void dispose() {
		panel.removeMouseListener(dragAndDropListener);
		super.dispose();
	}
	
	protected abstract void track(int px, int py, int qx, int qy, int iter);
}
