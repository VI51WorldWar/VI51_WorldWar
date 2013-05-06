package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.gui.minimap.MiniMap;
import fr.utbm.vi51.util.ImageManager;

/**
 * @author Top-K
 *
 */
public class Panel extends JPanel {
    private int displayedTilesX = 20;
    private int displayedTilesY = 20;
    private int originX;
    private int originY;
    
    private MiniMap minimap = null;

    public Panel() {
    	this.minimap = new MiniMap();
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    originY--;
                    originY = Math.max(0, originY);
                }

                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    originY++;
                    originY = Math.min(
                            Environment.getInstance().getMap()[0].length,
                            originY);
                }

                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    originX--;
                    originX = Math.max(0, originX);
                }

                if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    originX++;
                    originX = Math.min(
                            Environment.getInstance().getMap().length, originX);
                }

                if (ke.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    displayedTilesX++;
                    displayedTilesX = Math.min(displayedTilesX, Environment
                            .getInstance().getMap().length);
                    displayedTilesY++;
                    displayedTilesY = Math.min(displayedTilesY, Environment
                            .getInstance().getMap().length);
                }

                if (ke.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    displayedTilesX--;
                    displayedTilesX = Math.max(displayedTilesX, 1);
                    displayedTilesY--;
                    displayedTilesY = Math.max(displayedTilesY, 1);
                }

                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
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
                g.drawImage(
                        ImageManager.getInstance().getImage(
                                map[i + originX][j + originY][0].getLandType()
                                        .getTexturePath()), tileSizeX * i,
                        tileSizeY * j, tileSizeX, tileSizeY, this);
                // Draw World's objects on the square
                List<WorldObject> objs = map[i + originX][j + originY][0]
                        .getObjects();
                for (int k = 0; k < objs.size(); ++k) {
                    WorldObject obj = objs.get(k);
                    g.drawImage(
                            ImageManager.getInstance().getImage(
                                    obj.getTexturePath()),
                            (int) (tileSizeX * (obj.getPosition().getX() - originX))
                                    + k * tileSizeX / objs.size(),
                            (int) (tileSizeY * (obj.getPosition().getY() - originY)),
                            tileSizeX / 3, tileSizeY / 3, this);
                }
            }
        }
        Rectangle viewRect = new Rectangle(this.originX,this.originY,this.displayedTilesX,this.displayedTilesY);
        // Draw minimap
        this.minimap.paint(g, this,viewRect);
    }
}
