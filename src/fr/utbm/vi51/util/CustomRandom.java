package fr.utbm.vi51.util;

import java.util.Random;

/**
 * @author Top-K
 *
 */
public final class CustomRandom {
    private static Random randomGenerator;

    private CustomRandom() {
    }

    public Random getInstance() {
        if (randomGenerator == null) {
            randomGenerator = new Random();
        }
        return randomGenerator;
    }

    public static int getNextInt(int n) {
        if (randomGenerator == null) {
            randomGenerator = new Random();
        }
        return randomGenerator.nextInt(n);
    }
}
