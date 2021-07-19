package model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class OrderDetail {
    private String id;
    private String userId;
    private String orderId;
    private String foodId;
    private int quantity;
    private Date time;
    private boolean isSeen ;
    private boolean isAccepted ;
    private boolean isInBuffet;

    public OrderDetail() {
    }

    public OrderDetail(String id,String orderId, String userId, String foodId, int quantity, Date time,boolean isSeen,boolean isAccepted, boolean isInBuffet) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.time = time;
        this.isSeen=isSeen;
        this.isAccepted=isAccepted;
        this.isInBuffet = isInBuffet;
    }

    public boolean getIsInBuffet() {
        return isInBuffet;
    }

    public void setIsInBuffet(boolean inBuffet) {
        isInBuffet = inBuffet;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean seen) {
        isSeen = seen;
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
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm");
        return format.format(time);
    }

    public void setTime(String time) {

        DateFormat format = new SimpleDateFormat("YYYYMMdd_hhmm a");
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
        DateFormat format = new SimpleDateFormat("YYYYMMdd_hhmm a");
        String d = format.format(time);
        result.put("time", d);
        result.put("isSeen",isSeen);
        result.put("isAccepted",isAccepted);
        result.put("isInBuffet",isInBuffet);
        return result;
    }
}
