package com.example.su21g3project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class GetTableActivity extends AppCompatActivity {
    private Button btnNext;
    private EditText noPp;
    private TextView prTime;
    private SeekBar slTime;
    private Button today,today1,today2,today3;
    private int fDay;

    private String getTime(Calendar date){
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        month++;
        int year = date.get(Calendar.YEAR);
        return day+"/"+month+"/"+year;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_table);
        today = findViewById(R.id.btnToday);
        today1 = findViewById(R.id.btnTomorrow);
        today2 = findViewById(R.id.btnAfterTomorrow);
        today3 = findViewById(R.id.btnNextAfterTomorrow);

        today.setOnClickListener(v -> {
            today.setBackgroundColor(Color.GRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            fDay = 1;
        });
        today1.setOnClickListener(v -> {
            today1.setBackgroundColor(Color.GRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            fDay = 2;
        });
        today2.setOnClickListener(v -> {
            today2.setBackgroundColor(Color.GRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            fDay = 3;
        });
        today3.setOnClickListener(v -> {
            today3.setBackgroundColor(Color.GRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            fDay = 4;
        });
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00"));

        int currentMinute = date.get(Calendar.MINUTE);
        int currentHour = date.get(Calendar.HOUR_OF_DAY);
        currentHour+=7;
        today.setText("Hôm nay"+
                "\n"+getTime(date));
        date.add(Calendar.DATE,1);
        today1.setText("Ngày mai"+
                "\n"+getTime(date));
        date.add(Calendar.DATE,1);
        today2.setText("Ngày kia"+
                "\n"+getTime(date));
        date.add(Calendar.DATE,1);
        today3.setText("Ngày kìa"+
                "\n"+getTime(date));


        btnNext = findViewById(R.id.btnNext);
        noPp = findViewById(R.id.txtNoPP);
        prTime = findViewById(R.id.txtPreviewTime);
        slTime = findViewById(R.id.seekBar_slTime);
        int now = currentHour*60 + currentMinute;
        slTime.setMax(1440);
        slTime.setMin(now);

        slTime.setKeyProgressIncrement(10);
        slTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int hour = progress / 60;
                int minute = progress - (hour*60);
                prTime.setText(hour+" : "+minute);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}