package com.example.su21g3project.Customer;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.R;

import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {
    private RadioButton radioVn,radioEn;
    private TextView sltLG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        radioVn = findViewById(R.id.rdVi);
        radioVn.setText("Vietnamese");
        radioEn = findViewById(R.id.rdEnglish);
        radioEn.setText("English");
        sltLG = findViewById(R.id.txtSelectedLanguage);
        sltLG.setText(R.string.selectedLanguage);

        radioVn.setOnClickListener(v -> {
                Locale locale = new Locale("vn");
                Locale.setDefault(locale);
                Resources resources = this.getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
        });
        radioEn.setOnClickListener(v -> {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Resources resources = this.getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
        });
    }
}