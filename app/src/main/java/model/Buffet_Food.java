package model;

public class Buffet_Food {
    private String id;
    private String buffetId;
    private String foodId;

    public Buffet_Food(String id, String buffetId, String foodId) {
        this.id = id;
        this.buffetId = buffetId;
        this.foodId = foodId;
    }

    public Buffet_Food() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(String buffetId) {
        this.buffetId = buffetId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
