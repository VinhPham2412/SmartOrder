package Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class Buffet implements Serializable {
    private String id;
    private String name;
    private float price;
    private String description;
    private String image;



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

    public Buffet(String id, String name, float price, String description,String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image=image;
    }

    public Buffet() {
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("price", price);
        result.put("description", description);
        result.put("image", image);
        return result;
    }
}
