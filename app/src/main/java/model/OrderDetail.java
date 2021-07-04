package model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class OrderDetail {
    private String id;
    private String userId;
    private String orderId;
    private int foodId;
    private int quantity;
    private Date time;
    private boolean isVerify = false;

    public OrderDetail() {
    }

    public OrderDetail(String id,String orderId, String userId, int foodId, int quantity, Date time) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("orderId",orderId);
        result.put("userId", userId);
        result.put("foodId", foodId);
        result.put("quantity", quantity);
        DateFormat format = new SimpleDateFormat("YYYYMMdd_hhmm a");
        String d = format.format(time);
        result.put("time", d);
        result.put("isVerify",isVerify);
        return result;
    }
}
