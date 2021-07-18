package model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Food {
    private String id;
    private String name;
    private float price;
    private String description;
    private int calories;
    private String image;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Food(String id, String name, float price, String description, int calories, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.calories = calories;
        this.image=image;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("name", name);
        result.put("price", price);
        result.put("description", description);
        result.put("calories", calories);
        result.put("image", image);
        result.put("type",type);
        return result;
    }

    public Food() {
    }

    public String getType() {
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
}
