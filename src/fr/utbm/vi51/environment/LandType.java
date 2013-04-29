package fr.utbm.vi51.environment;

public enum LandType {
    // Enumeration of different types of land available in the game
    GRASS(1, true, "img/grass.png"), WATER(2, true, "img/water.png"), WALL(0, false, "img/wall.png");

    // Cost to pass across this type
    private final int cost;
    // Is this type of floor crossable ?
    private final boolean isCrossable;
    // Path of the land's texture
    private final String texturePath;

    // Enumerator constructor
    LandType(int cost, boolean isCrossable, String texturePath) {
        this.cost = cost;
        this.isCrossable = isCrossable;
        this.texturePath = texturePath;
    }

    public int getCost() {
        return cost;
    }

    public boolean isCrossable() {
        return isCrossable;
    }

    public String getTexturePath() {
        return texturePath;
    }

}
