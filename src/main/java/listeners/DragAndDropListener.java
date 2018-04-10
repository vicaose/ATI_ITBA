package listeners;

import gui.Panel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextField;

public class DragAndDropListener implements MouseMotionListener, MouseListener {
	private JTextField px, py, qx, qy;
	private Panel panel;
	private Point startPoint, endPoint;
	
	public DragAndDropListener(Panel panel, JTextField px, JTextField py, JTextField qx, JTextField qy) {
		this.px = px;
		this.py = py;
		this.qx = qx;
		this.qy = qy;
		this.panel = panel;
	}
		
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("released ("+e.getX()+","+e.getY()+")");
		qx.setText(String.valueOf(e.getX()));
		qy.setText(String.valueOf(e.getY()));
		drawRectangle();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("pressed ("+e.getX()+","+e.getY()+")");
		px.setText(String.valueOf(e.getX()));
		py.setText(String.valueOf(e.getY()));
		startPoint = e.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		endPoint = e.getPoint();
		drawRectangle();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void drawRectangle() {
        Graphics2D g2 = (Graphics2D)panel.getGraphics();
        //BufferedImage img = panel.getImage().getBufferedImage();
        //g2.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);

        Rectangle2D prostokat = new Rectangle2D.Double();
        prostokat.setFrameFromDiagonal(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        panel.repaint();
        g2.draw(prostokat);
        g2.setColor(new Color(0, 0, 255, 40));
        g2.fill(prostokat);
        g2.dispose();
        panel.repaint();
	}
}
