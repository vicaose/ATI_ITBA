package gui;

import gui.menus.MenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private Panel panel = new Panel(this);
	private MenuBar menuBar = new MenuBar();
    private GraphicsConfiguration config;
    private Point location;
    
	public Window() {
        config = getGraphicsConfiguration();
		setTitle("Images Analysis and Treatment");
		setBounds(1, 1, 1000, 800);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		location = new Point(size.width / 3 - getWidth() / 3, size.height / 3
				- getHeight() / 2);
		setLocation(location);
		setResizable(true);
		setMinimumSize(new Dimension(1000, 800));
		panel.setBackground(Color.WHITE);
		setJMenuBar(menuBar);
        panel.initKeyBindings();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		add(panel);
	}

	public Point getLocation() {
		return location;
	}
	
	public Panel getPanel() {
		return panel;
	}

    public GraphicsConfiguration getConfig() {
        return config;
    }
}
