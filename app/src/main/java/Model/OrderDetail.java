package Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class OrderDetail {
    @Exclude
    private DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
    private String id;
    private String userId;
    private String orderId;
    private String foodId;
    private int quantity;
    private Date time;
    private String status;
    private String reason;
    boolean isInBuffet;


    public OrderDetail() {
    }

    public OrderDetail(String id,String orderId, String userId, String foodId, int quantity, Date time,boolean isInBuffet,String status) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.time = time;
        this.isInBuffet = isInBuffet;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
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
    public String getTimeString(){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(time);
    }

    public void setTime(String time) {
        try{
            this.time = format.parse(time);
        }catch (ParseException e){
        }
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("orderId",orderId);
        result.put("userId", userId);
        result.put("foodId", foodId);
        result.put("quantity", quantity);
        String d = format.format(time);
        result.put("time", d);
        result.put("isInBuffet",isInBuffet);
        result.put("status",status);
        return result;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return  reason;
    }

    public boolean getIsInBuffet() {
        return isInBuffet;
    }
    public void setIsInBuffet(boolean isInBuffet) {
        this.isInBuffet = isInBuffet;
    }
}
