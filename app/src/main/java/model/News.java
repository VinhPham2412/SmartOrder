package model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class News{
    private String id;
    private Date time;
    private String title;
    private String content;
    private String image;

    public News() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }
    public String getStrTime(){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.format(time);
    }

    public void setTime(String time) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
        try{
            this.time = format.parse(time);
        }catch (ParseException e){

        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("title", title);
        result.put("content", content);
        DateFormat format = new SimpleDateFormat("YYYYMMdd_HHmm");
        String d = format.format(time);
        result.put("time",d);
        result.put("image",image);

        return result;
    }
}
