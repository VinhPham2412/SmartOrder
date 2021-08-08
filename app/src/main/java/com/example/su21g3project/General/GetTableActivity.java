package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.R;
import com.scwang.wave.MultiWaveHeader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private MultiWaveHeader waveHeader;
    private DateFormat format;

    private String getTime(Calendar date){
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);
        //because january was 0 then need add by 1
        month++;
        int year = date.get(Calendar.YEAR);
        return day+"/"+month+"/"+year;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_table);

        waveHeader=findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.BLACK);
        waveHeader.setCloseColor(Color.GRAY);

        date = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00"));
        format = new SimpleDateFormat("yyyyMMdd_HHmm");
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
        String td = getString(R.string.today);
        String td1 = getString(R.string.tomorrow);
        String td2 = getString(R.string.tomorrow1);
        String td3 = getString(R.string.tomorrow2);
        today3.setText(td3+
                "\n"+getTime(date));
        date.add(Calendar.DAY_OF_MONTH,-1);
        today2.setText(td2+
                "\n"+getTime(date));
        date.add(Calendar.DAY_OF_MONTH,-1);
        today1.setText(td1+
                "\n"+getTime(date));
        date.add(Calendar.DAY_OF_MONTH,-1);
        today.setText(td+
                "\n"+getTime(date));



        today.setSelected(true);
        today.setBackgroundColor(Color.LTGRAY);
        today1.setBackgroundColor(Color.WHITE);
        today2.setBackgroundColor(Color.WHITE);
        today3.setBackgroundColor(Color.WHITE);
        slTime.setMin(currentHour*60 + currentMinute + 30);
        selectedDate = format.format(date.getTime());
        hour = slTime.getProgress() / 60;
        minute = slTime.getProgress() - (hour*60);
        prTime.setText(hour+" : "+minute);

        today.setOnClickListener(v -> {
            today.setBackgroundColor(Color.LTGRAY);
            today1.setBackgroundColor(Color.WHITE);
            today2.setBackgroundColor(Color.WHITE);
            today3.setBackgroundColor(Color.WHITE);
            selectedDate = format.format(date.getTime());
            slTime.setMin(currentHour*60 + currentMinute+ 30);
        });
        today1.setOnClickListener(v -> {
            today1.setBackgroundColor(Color.LTGRAY);
            today.setBackgroundColor(Color.WHITE);
            today2.setBackgroundColor(Color.WHITE);
            today3.setBackgroundColor(Color.WHITE);
            date.add(Calendar.DAY_OF_MONTH,1);
            selectedDate = format.format(date.getTime());
            date.add(Calendar.DAY_OF_MONTH,-1);
            slTime.setMin(1);
        });
        today2.setOnClickListener(v -> {
            today2.setBackgroundColor(Color.LTGRAY);
            today3.setBackgroundColor(Color.WHITE);
            today.setBackgroundColor(Color.WHITE);
            today1.setBackgroundColor(Color.WHITE);
            date.add(Calendar.DAY_OF_MONTH,2);
            selectedDate = format.format(date.getTime());
            date.add(Calendar.DAY_OF_MONTH,-2);
            slTime.setMin(1);
        });
        today3.setOnClickListener(v -> {
            today3.setBackgroundColor(Color.LTGRAY);
            today.setBackgroundColor(Color.WHITE);
            today1.setBackgroundColor(Color.WHITE);
            today2.setBackgroundColor(Color.WHITE);
            date.add(Calendar.DAY_OF_MONTH,3);
            selectedDate = format.format(date.getTime());
            date.add(Calendar.DAY_OF_MONTH,-3);
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
            String numberOfPeople = noPp.getText().toString();
            if(numberOfPeople!=null&&!numberOfPeople.trim().isEmpty()){
                Intent intent = new Intent(GetTableActivity.this, GetTableActivity2.class);
                //transfer date,time,number of people to next view
                //combine date and time to date type
                intent.putExtra("date",selectedDate);
                intent.putExtra("noPP",numberOfPeople);
                startActivity(intent);
            }else {
                Toast.makeText(this,getString(R.string.toastGettable)
                        ,Toast.LENGTH_SHORT).show();
                noPp.requestFocus();
            }
        });
    }
}