package com.example.pagetrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int images[]={
            R.raw.bybkk,
            R.raw.rdpd,
            R.raw.epay
    };
    int headings[]={
            R.string.heading1,
            R.string.heading2,
            R.string.heading3
    };
    int desc[]={
            R.string.desc1,
            R.string.desc2,
            R.string.desc3
    };
    public ViewPagerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return headings.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout) object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater=(LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slider_layout,container,false);

        LottieAnimationView lottieAnimationView = view.findViewById(R.id.titleImage);
        lottieAnimationView.setAnimation(images[position]);
        lottieAnimationView.playAnimation();

        //ImageView slideTitleImage=(ImageView) view.findViewById(R.id.titleImage);
        TextView slideHeading=(TextView) view.findViewById(R.id.texttitle);
        TextView slideDesc=(TextView) view.findViewById(R.id.texdesc);

        //lottieAnimationView.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDesc.setText(desc[position]);

        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
