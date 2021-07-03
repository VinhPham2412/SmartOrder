package Waiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.su21g3project.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import adapter.ViewPagerAdapter;

public class MainWaiterActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_waiter);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Trang chủ");
                        break;
                    case 1:
                        tab.setText("Hóa Đơn");
                        break;
                    case 2:
                        tab.setText("Lịch sử gọi món");
                        break;
                    case 3:
                        tab.setText("Giao tiếp");
                        break;
                    case 4:
                        tab.setText("Bàn chờ");
                        break;
                    case 5:
                        tab.setText("Gọi món");
                        break;

                }
            }
        }).attach();

    }
}