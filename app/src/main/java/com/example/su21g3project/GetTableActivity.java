package com.example.su21g3project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GetTableActivity extends AppCompatActivity {
    private Button btnNext;
    private EditText noPp;
    private TextView prTime;
    private SeekBar slTime;
    private String selectedDate;
    private Button today,today1,today2,today3;
    private Calendar date;
    private int hour,minute,currentMinute,currentHour;

    private String getTime(Calendar date){
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);
        month++;
        int year = date.get(Calendar.YEAR);
        return day+"/"+month+"/"+year;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_table);
        date = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00"));
        DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
        currentMinute = date.get(Calendar.MINUTE);
        currentHour = date.get(Calendar.HOUR_OF_DAY);
        currentHour+=7;

        today = findViewById(R.id.btnToday);
        today1 = findViewById(R.id.btnTomorrow);
        today2 = findViewById(R.id.btnAfterTomorrow);
        today3 = findViewById(R.id.btnNextAfterTomorrow);
        prTime = findViewById(R.id.txtPreviewTime);
        slTime = findViewById(R.id.seekBar_slTime);
        slTime.setMax(1440);
        date.add(Calendar.DAY_OF_MONTH,3);
        today3.setText("Ngày kìa"+
                "\n"+getTime(date));
        date.add(Calendar.DAY_OF_MONTH,-1);
        today2.setText("Ngày kia"+
                "\n"+getTime(date));
        date.add(Calendar.DAY_OF_MONTH,-1);
        today1.setText("Ngày mai"+
                "\n"+getTime(date));
        date.add(Calendar.DAY_OF_MONTH,-1);
        today.setText("Hôm nay"+
                "\n"+getTime(date));



        today.setSelected(true);
        today.setBackgroundColor(Color.GRAY);
        today1.setBackgroundColor(Color.LTGRAY);
        today2.setBackgroundColor(Color.LTGRAY);
        today3.setBackgroundColor(Color.LTGRAY);
        slTime.setMin(1);
        selectedDate = format.format(date.getTime());

        today.setOnClickListener(v -> {
            today.setBackgroundColor(Color.GRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            date.add(Calendar.DAY_OF_MONTH,-3);
            selectedDate = format.format(date.getTime());
            slTime.setMin(currentHour*60 + currentMinute);
            date.add(Calendar.DATE,3);
        });
        today1.setOnClickListener(v -> {
            today1.setBackgroundColor(Color.GRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            date.add(Calendar.DAY_OF_MONTH,-2);
            selectedDate = format.format(date.getTime());
            slTime.setMin(1);
            date.add(Calendar.DAY_OF_MONTH,2);
        });
        today2.setOnClickListener(v -> {
            today2.setBackgroundColor(Color.GRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            date.add(Calendar.DAY_OF_MONTH,-1);
            selectedDate = format.format(date.getTime());
            slTime.setMin(1);
            date.add(Calendar.DAY_OF_MONTH,1);
        });
        today3.setOnClickListener(v -> {
            today3.setBackgroundColor(Color.GRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            selectedDate = format.format(date.getTime());
            slTime.setMin(1);
        });
        btnNext = findViewById(R.id.btnNext);
        noPp = findViewById(R.id.txtNoPP);
        slTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hour = progress / 60;
                minute = progress - (hour*60);
                prTime.setText(hour+" : "+minute);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(GetTableActivity.this,GetTableActivity2.class);
            //transfer date,time,number of people to next view
            //combine date and time to date type
            intent.putExtra("date",selectedDate);
            intent.putExtra("noPP",noPp.getText().toString());

            startActivity(intent);
        });
    }
}