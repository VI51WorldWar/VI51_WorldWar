package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.WorldObject;

public class Panel extends JPanel {
    public void paintComponent(Graphics g) {
        try {
            Environment env = Environment.getInstance();
            Image img = ImageIO.read(new File("img/grass.png"));
            for (int i = 0; i < 800 / 50; i++) {
                for (int j = 0; j < 600 / 44; j++) {
                    g.drawImage(img, 50 * i, 44 * j, 50, 50, this);

                }
            }
            for (WorldObject obj : env.getObjects()) {
                Image img1 = ImageIO.read(new File(obj.getTexture()));
                g.drawImage(img1, (int) (50 * obj.getPosition().getX()), (int) (44 * obj.getPosition().getY()), 20, 20, this);
            }
            // Pour une image de fond
            // g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
