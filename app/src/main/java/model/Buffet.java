package model;

import java.io.Serializable;

public class Buffet implements Serializable {
    private int id;
    private String name;
    private float price;
    private String description;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Buffet(int id, String name, float price, String description,String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image=image;
    }

    public Buffet() {
    }
}
