package com.example.pagetrade;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;
    public LoginAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position) {
        switch(position) {
            case 0:
               // LoginTabFragment loginTabFragment=new LoginTabFragment();
                return new LoginTabFragment();
            case 1:
               // SignupTabFragment signupTabFragment=new SignupTabFragment();
                return new SignupTabFragment();
            default:
                return null;
        }
    }
}
