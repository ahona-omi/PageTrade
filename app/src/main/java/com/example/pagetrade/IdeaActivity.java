package com.example.pagetrade;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class IdeaActivity extends AppCompatActivity {
    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button back,next,skip;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);

        back=findViewById(R.id.backBtn);
        next=findViewById(R.id.nxtBtn);
        skip=findViewById(R.id.skipBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getitem(0)>0){
                    mSlideViewPager.setCurrentItem(getitem(-1),true);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getitem(0)<2)
                    mSlideViewPager.setCurrentItem(1,true);
                else {
                    Intent i = new Intent(IdeaActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IdeaActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        mSlideViewPager=(ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout=(LinearLayout) findViewById(R.id.indicator_layout);
        viewPagerAdapter= new ViewPagerAdapter(this);
        mSlideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }
    public void setUpIndicator(int position) {
        dots=new TextView[3];
        mDotLayout.removeAllViews();

        for (int i=0; i<dots.length;i++) {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white, getApplication().getTheme()));
            mDotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.lavender0, getApplication().getTheme()));
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);
            if (position>0){
                back.setVisibility(View.VISIBLE);
            } else{
                back.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private int getitem(int i){
        return mSlideViewPager.getCurrentItem()+i;
    }
}