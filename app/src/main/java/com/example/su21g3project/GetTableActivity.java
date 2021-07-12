package com.example.su21g3project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GetTableActivity extends AppCompatActivity {
    private Button btnNext;
    private EditText noPp;
    private TextView prTime;
    private SeekBar slTime;
    private Button today,today1,today2,today3;
    private Calendar finalDate,d,d1,d2,d3;
    private int hour,minute,currentMinute,currentHour;

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
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00"));
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

        today.setSelected(true);
        today.setBackgroundColor(Color.GRAY);
        today1.setBackgroundColor(Color.LTGRAY);
        today2.setBackgroundColor(Color.LTGRAY);
        today3.setBackgroundColor(Color.LTGRAY);
        slTime.setMin(currentHour*60 + currentMinute);
        finalDate = d;

        today.setOnClickListener(v -> {
            today.setBackgroundColor(Color.GRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            finalDate = d;
            slTime.setMin(currentHour*60 + currentMinute);
        });
        today1.setOnClickListener(v -> {
            today1.setBackgroundColor(Color.GRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            finalDate = d1;
            slTime.setMin(1);
        });
        today2.setOnClickListener(v -> {
            today2.setBackgroundColor(Color.GRAY);
            today3.setBackgroundColor(Color.LTGRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            finalDate = d2;
            slTime.setMin(1);
        });
        today3.setOnClickListener(v -> {
            today3.setBackgroundColor(Color.GRAY);
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.LTGRAY);
            today2.setBackgroundColor(Color.LTGRAY);
            finalDate = d3;
            slTime.setMin(1);
        });
        today.setText("Hôm nay"+
                "\n"+getTime(date));
        d = date;
        date.add(Calendar.DATE,1);
        today1.setText("Ngày mai"+
                "\n"+getTime(date));
        d1 = date;
        date.add(Calendar.DATE,1);
        today2.setText("Ngày kia"+
                "\n"+getTime(date));
        d2 = date;
        date.add(Calendar.DATE,1);
        today3.setText("Ngày kìa"+
                "\n"+getTime(date));
        d3 = date;


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
            int tMonth = finalDate.get(Calendar.MONTH);
            GregorianCalendar gregorianCalendar = new GregorianCalendar(finalDate.get(Calendar.YEAR),tMonth,finalDate.get(Calendar.DATE),hour,minute,0);
            Date finalDateAndTime = gregorianCalendar.getTime();
            intent.putExtra("date",finalDateAndTime.getTime());
            intent.putExtra("noPP",noPp.getText().toString());

            startActivity(intent);
        });
    }
}