package Model;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Bill {
    @Exclude
    private DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
    private String id;
    private String orderId;
    private Float totalMoney;
    private HashMap<String,Object> details;
    private Date time;
    private String tableId;
    private String buffetId;

    public Bill() {
    }

    public Bill(String id, String orderId, Float totalMoney, HashMap<String, Object> details, Date time, String tableId,String buffetId) {
        this.id = id;
        this.orderId = orderId;
        this.totalMoney = totalMoney;
        this.details = details;
        this.time = time;
        this.tableId = tableId;
        this.buffetId = buffetId;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("orderId", orderId);
        result.put("totalMoney", totalMoney);
        result.put("details", details);
        String d = format.format(time);
        result.put("time", d);
        result.put("tableId", tableId);
        result.put("buffetId", buffetId);
        return result;
    }

    public String getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(String buffetId) {
        this.buffetId = buffetId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public HashMap<String, Object> getDetails() {
        return details;
    }

    public void setDetails(HashMap<String, Object> details) {
        this.details = details;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(String time) {
        try {
            this.time = format.parse(time);
        } catch (ParseException e) {
            Log.println(Log.ERROR, "parse error", e.getMessage());
        }
    }

    public String getStrTime() {
        return time!= null? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(time):"Lỗi khi xử lý thời gian";
    }
}
