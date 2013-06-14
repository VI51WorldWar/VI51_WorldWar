package fr.utbm.vi51.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @author Theo
 * Ensure that images are loaded only once.
 * All objects using images must request it to the image manager and not load it themselves.
 * Singleton class.
 */
public final class ImageManager {
    private static ImageManager instance;
    private Map<String, Image> images;

    private ImageManager() {
        images = new HashMap<String, Image>();
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }

        return instance;
    }

    public Image getImage(String path) {
        if (!images.containsKey(path)) {
            try {
                images.put(path, ImageIO.read(new File(path)));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return images.get(path);
    }
}
