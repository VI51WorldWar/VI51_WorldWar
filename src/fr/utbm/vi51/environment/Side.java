package fr.utbm.vi51.environment;

import java.awt.Color;

/**
 * @author Top-K
 *
 */
public class Side {
    private int foodAmount;
    private int id;
    private String workerTexture;
    private String warriorTexture;
    private String queenTexture;
    private Color dominantColor;

    public Side(int id, String worker, String warrior, String queen,
            Color dominantColor) {
        assert dominantColor != null;
        this.id = id;
        this.workerTexture = worker;
        this.warriorTexture = warrior;
        this.queenTexture = queen;
        this.dominantColor = dominantColor;
    }

    public String getWorkerTexture() {
        return this.workerTexture;
    }

    public void setWorkerTexture(String workerTexture) {
        this.workerTexture = workerTexture;
    }

    public String getWarriorTexture() {
        return this.warriorTexture;
    }

    public void setWarriorTexture(String warriorTexture) {
        this.warriorTexture = warriorTexture;
    }

    public String getQueenTexture() {
        return this.queenTexture;
    }

    public void setQueenTexture(String queenTexture) {
        this.queenTexture = queenTexture;
    }

    public int getFoodAmount() {
        return this.foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Side other = (Side) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Color getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(Color dominantColor) {
        this.dominantColor = dominantColor;
    }

}
