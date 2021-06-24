package model;

public class Food {
    private int id;
    private String name;
    private float price;
    private String description;
    private int calories;
    private int categoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Food(int id, String name, float price, String description, int calories, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.calories = calories;
        this.categoryId = categoryId;
    }

    public Food() {
    }
}
