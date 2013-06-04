package fr.utbm.vi51.environment;

public class Side {
    private int foodAmount = 0;
    private int id;
    private String workerTexture;
    private String warriorTexture;
    private String queenTexture;
    
    public Side(int id, String worker, String warrior, String queen){
        this.id = id;
        this.workerTexture = worker;
        this.warriorTexture = warrior;
        this.queenTexture = queen;
    }

    public String getWorkerTexture() {
        return workerTexture;
    }

    public void setWorkerTexture(String workerTexture) {
        this.workerTexture = workerTexture;
    }

    public String getWarriorTexture() {
        return warriorTexture;
    }

    public void setWarriorTexture(String warriorTexture) {
        this.warriorTexture = warriorTexture;
    }

    public String getQueenTexture() {
        return queenTexture;
    }

    public void setQueenTexture(String queenTexture) {
        this.queenTexture = queenTexture;
    }

    public int getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Side other = (Side) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    
}
