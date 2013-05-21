package fr.utbm.vi51.environment;

public class Side {
    private int foodAmount = 0;
    private int id;
    
    public Side(int id){
        this.id = id;
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
    
}
