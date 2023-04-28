package com.padme.emaone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;

public class reg_0 extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager2 viewPager2;

    private SignupAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg0);


        FragmentManager fragmentManager = getSupportFragmentManager();


        tablayout=findViewById(R.id.tab_lay_01);
        viewPager2=findViewById(R.id.view_pager01);

        tablayout.addTab(tablayout.newTab().setText("Login"));
        tablayout.addTab(tablayout.newTab().setText("Signup"));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter=new SignupAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(adapter);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tablayout.selectTab(tablayout.getTabAt(position));
            }
        });

        // final SignupAdapter adapter = new SignupAdapter(getSupportFragmentManager(),this,tablayout.getTabCount());


    }
}