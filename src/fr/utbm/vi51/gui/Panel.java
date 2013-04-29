package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panel extends JPanel {
    public void paintComponent(Graphics g) {
        try {
            Image img = ImageIO.read(new File("img/grass.png"));
            for (int i = 0; i < 800 / 50; i++) {
                for (int j = 0; j < 600 / 44; j++) {
                    g.drawImage(img, 50 * i, 44 * j, 50, 50, this);

                }
            }
            // Pour une image de fond
            // g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
