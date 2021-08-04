package model;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ProcessOrder {
    @Exclude
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private String id;
    private String userId;
    private String name;
    private String phone;
    private Date date;
    private int numberOfPeople;
    private String note;
    private String status;
    private String buffetId ;
    private String tableId;

    public ProcessOrder() {
    }

    public ProcessOrder(String id,@Nullable String userId, String name, String phone,
                        Date date, int numberOfPeople, String note,String status, @Nullable String tableId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.numberOfPeople = numberOfPeople;
        this.note = note;
        this.status=status;
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(String buffetId) {
        this.buffetId = buffetId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getDate() {
        return date;
    }
    public String getStrDate(){
        return format.format(date);
    }

    public void setDate(String date) {
        try{
            this.date = format.parse(date);
            Log.println(Log.INFO,"parse info - ","date = "+this.date.toString());
        }catch (ParseException e){
            Log.println(Log.ERROR,"parse error",e.getMessage());
        }
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("userId", userId);
        result.put("name", name);
        result.put("phone", phone);
        String d = format.format(date);
        result.put("date",d);
        result.put("numberOfPeople", numberOfPeople);
        result.put("note", note);
        result.put("status",status);
        result.put("tableId",null);

        return result;
    }
}
