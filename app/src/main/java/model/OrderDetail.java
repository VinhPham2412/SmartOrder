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
    @Exclude
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private String id;
    private String userId;
    private String orderId;
    private String foodId;
    private int quantity;
    private Date time;
    private boolean isSeen;
    private boolean isAccepted ;
    private boolean isInBuffet;
    private boolean doing;

    public boolean isDoing() {
        return doing;
    }

    public void setDoing(boolean doing) {
        this.doing = doing;
    }

    public OrderDetail() {
    }

    public OrderDetail(String id,String orderId, String userId, String foodId, int quantity, Date time,boolean isSeen,boolean isAccepted, boolean isInBuffet,boolean doing) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.time = time;
        this.isSeen=isSeen;
        this.isAccepted=isAccepted;
        this.isInBuffet = isInBuffet;
        this.doing=doing;
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
        return format.format(time);
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
        result.put("isSeen",isSeen);
        result.put("isAccepted",isAccepted);
        result.put("isInBuffet",isInBuffet);
        result.put("doing",doing);
        return result;
    }
}
