package fr.utbm.vi51.environment;


/**
 * @author Top-K
 *
 */
public enum LandType {
    // Enumeration of different types of land available in the game
    GRASS("Grass", 1, true, "img/Tiles/grass.png"), 
    SAND("Sand", 1,true, "img/Tiles/sand.png"),
    WATER("Water", 4, true, "img/Tiles/water.png"), 
    WALL("Wall", 0, false, "img/Tiles/wall.png"),
    CAVE("Cave", 1,true, "img/Tiles/cave.png"),
    STAIR("Stair", 1,true, "img/Tiles/stair.png"),
    WATERFALL("Waterfall", 0,false, "img/Tiles/waterfall.png"),
    CLIFF("Cliff", 0,false, "img/Tiles/cliff.png");

    // Cost to pass across this type
    private final int cost;
    // Is this type of floor crossable ?
    private final boolean isCrossable;
    // Path of the land's texture
    private final String texturePath;
    // Name of the landType
    private final String name;
    // Enumerator constructor
    LandType(String name, int cost, boolean isCrossable, String texturePath) {
        this.name = name;
        this.cost = cost;
        this.isCrossable = isCrossable;
        this.texturePath = texturePath;

    }

    public int getCost() {
        return this.cost;
    }

    public boolean isCrossable() {
        return this.isCrossable;
    }

    public String getTexturePath() {
        return this.texturePath;
    }

    public String getName() {
        return this.name;
    }
}
