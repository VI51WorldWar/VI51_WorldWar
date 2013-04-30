package fr.utbm.vi51.environment;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Top-K
 *
 */
public enum LandType {
    // Enumeration of different types of land available in the game
    GRASS(1, true, "img/Tiles/grass.png"), WATER(2, true, "img/Tiles/water.png"), WALL(
            0, false, "img/Tiles/wall.png");

    // Cost to pass across this type
    private final int cost;
    // Is this type of floor crossable ?
    private final boolean isCrossable;
    // Path of the land's texture
    private final Image texture;

    // Enumerator constructor
    LandType(int cost, boolean isCrossable, String texturePath) {
        this.cost = cost;
        this.isCrossable = isCrossable;
        Image tmp;
        try {
            tmp = ImageIO.read(new File(texturePath));
        } catch (IOException e) {
            tmp = null;
            e.printStackTrace();
        }
        this.texture = tmp;

    }

    public int getCost() {
        return cost;
    }

    public boolean isCrossable() {
        return isCrossable;
    }

    public Image getTexture() {
        return texture;
    }

}
