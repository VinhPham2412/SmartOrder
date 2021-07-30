package com.example.su21g3project.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.su21g3project.DownloadImageTask;
import com.example.su21g3project.R;

public class NewsActivity extends AppCompatActivity {
private TextView title,content;
private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        String imgUrl = getIntent().getStringExtra("image");
        String ct = getIntent().getStringExtra("content");
        String tt = getIntent().getStringExtra("title");
        title = findViewById(R.id.txtTitle);
        content = findViewById(R.id.txtContent);
        image = findViewById(R.id.imageView4);

        new DownloadImageTask(image).execute(imgUrl);
        title.setText(tt);
        content.setText(ct);
    }
}