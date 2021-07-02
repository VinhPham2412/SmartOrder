package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CommunicationCustomer extends AppCompatActivity {
    private EditText txtReason;
    private ListView listviewdata;
    ArrayAdapter<String> adapter;
    private Button btnSenReason,btnCamera;
    String [] arrayPeliculas={"Đồ ăn ra quá chậm","Đồ ăn ra không đúng với Order","Chất lượng đồ ăn có vấn đề",
            "Thái độ nhân viên không được chuẩn mực","Vệ sinh bàn còn rất bẩn"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_customer);
        btnSenReason=findViewById(R.id.btnSendReason);
        txtReason=findViewById(R.id.txtReason);
        listviewdata=findViewById(R.id.listview_data);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,arrayPeliculas);
        listviewdata.setAdapter(adapter);
        btnSenReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_communication,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.item_done){
            String itemSelected="";
          for (int i=0;i<listviewdata.getCount();i++){
              if (listviewdata.isItemChecked(i)){
                  itemSelected+=listviewdata.getItemAtPosition(i)+"\n";
              }
          }
          txtReason.setText(itemSelected);
        }
        return super.onOptionsItemSelected(item);
    }
}