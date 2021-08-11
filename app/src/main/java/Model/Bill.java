package Model;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
@IgnoreExtraProperties
public class Bill {
    @Exclude
    private DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
    private String id;
    private String orderId;
    private Float totalMoney;
    private HashMap<String,Object> details;
    private Date time;

    public Bill() {
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
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(time);
    }
}
