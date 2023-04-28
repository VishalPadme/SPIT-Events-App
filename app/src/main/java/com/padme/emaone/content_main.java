package com.padme.emaone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class content_main extends AppCompatActivity {
    private MeowBottomNavigation bnv_Main;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        context=getApplicationContext();

        bnv_Main = findViewById(R.id.meowBottomNavigation);
        bnv_Main.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_home_24));
        bnv_Main.add(new MeowBottomNavigation.Model(2,R.drawable.ic_baseline_event_note_24));
        bnv_Main.add(new MeowBottomNavigation.Model(3,R.drawable.baseline_subscriptions_24));
        bnv_Main.add(new MeowBottomNavigation.Model(4,R.drawable.ic_baseline_account_circle_24));
        bnv_Main.show(1,true);
        Fragment t1 =new HomePageEvents();
        Fragment t2 =new AllEventsPage();
       Fragment t3 =new SubEventsPage();
       Fragment t4 =new UserFrag();
        replace(t1);
        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        replace(t1);
                        break;

                    case 2:
                        replace(t2);
                        break;
                    case 3:
                        replace(t3);
                        break;

                    case 4:
                        replace(t4);
                        break;
                }

                return null;
            }
        });
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_frame, fragment);
        transaction.commit();

    }
}