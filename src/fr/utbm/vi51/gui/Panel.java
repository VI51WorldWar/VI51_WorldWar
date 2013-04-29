package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;

public class Panel extends JPanel {
	private int displayedTilesX = 20;
	private int displayedTilesY = 20;
	private int originX = 0;
	private int originY = 0;

	public Panel() {
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_UP) {
					originY--;
					originY = Math.max(0, originY);
				}
				
				if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
					originY++;
					originY = Math.min(Environment.getInstance().getMap()[0].length, originY);
				}
				
				if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
					originX--;
					originX = Math.max(0, originX);
				}
				
				if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
					originX++;
					originX = Math.min(Environment.getInstance().getMap().length, originX);
				}
				
				if (ke.getKeyCode() == KeyEvent.VK_PAGE_UP) {
					displayedTilesX++;
					displayedTilesX = Math.min(displayedTilesX, Environment.getInstance().getMap().length);
					displayedTilesY++;
					displayedTilesY = Math.min(displayedTilesY, Environment.getInstance().getMap().length);
				}

				if (ke.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
					System.out.println("pouet");
					displayedTilesX--;
					displayedTilesX = Math.max(displayedTilesX, 0);
					displayedTilesY--;
					displayedTilesY = Math.max(displayedTilesY, 0);
				}
			}
		});
	}

	public void paintComponent(Graphics g) {
		int tileSizeX = this.getWidth() / displayedTilesX;
		int tileSizeY = this.getHeight() / displayedTilesY;

		Environment env = Environment.getInstance();
		Square[][][] map = env.getMap();
		for (int i = 0; i < displayedTilesX; i++) {
			for (int j = 0; j < displayedTilesY; j++) {
				// Draw land
				g.drawImage(map[i + originX][j + originY][0].getLandType()
						.getTexture(), tileSizeX * i, tileSizeY * j, tileSizeX,
						tileSizeY, this);
				// Draw World's objects on the square
				for (WorldObject obj : map[i + originX][j + originY][0]
						.getObjects()) {
					g.drawImage(obj.getTexture(), (int) (tileSizeX * obj
							.getPosition().getX()), (int) (tileSizeY * obj
							.getPosition().getY()), tileSizeX, tileSizeY, this);
				}
			}
		}
	}
}
