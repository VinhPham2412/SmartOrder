package model;

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
    private String id;
    private String userId;
    private String name;
    private String phone;
    private Date date;
    private int numberOfPeople;
    private String note;
    private boolean isVerify = false;

    public ProcessOrder() {
    }

    public ProcessOrder(String id,@Nullable String userId, String name, String phone,
                        Date date, int numberOfPeople, String note, boolean isVerify) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.numberOfPeople = numberOfPeople;
        this.note = note;
        this.isVerify = isVerify;
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

    public boolean getIsVerify() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        DateFormat format = new SimpleDateFormat("YYYYMMdd_hhmm a");
        try{
            this.date = format.parse(date);
        }catch (ParseException e){

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
        DateFormat format = new SimpleDateFormat("YYYYMMdd_hhmm a");
        String d = format.format(date);
        result.put("date",d);
        result.put("numberOfPeople", numberOfPeople);
        result.put("note", note);
        result.put("isVerify", isVerify);
        result.put("status",false);

        return result;
    }
}
