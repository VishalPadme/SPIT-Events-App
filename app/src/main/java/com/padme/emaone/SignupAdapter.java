package com.padme.emaone;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SignupAdapter extends FragmentStateAdapter {

    private Context context;
    int tabscaount;

    /*public SignupAdapter(FragmentManager fm,Context context,int tabs)
    {
        super(fm);
        this.context=context;
        this.tabscaount=tabs;
    }*/

    public SignupAdapter( FragmentManager fragmentManager,Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new SignupTabFragment();
        }
        return new LoginTabFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

   /* @Override
    public int getCount() {
        return tabscaount;
    }

    public Fragment getItem(int pos){
        switch (pos){
            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return null;

        }
    }*/
}
