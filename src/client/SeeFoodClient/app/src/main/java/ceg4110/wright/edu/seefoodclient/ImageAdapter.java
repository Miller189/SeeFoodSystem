package ceg4110.wright.edu.seefoodclient;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImageAdapter extends PagerAdapter {
    Context context;
    private int[] GalImages = new int[] {
            0, 1, 2
    };
    ImageAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public ImageView instantiateItem(ViewGroup container, int position) {
        ImageView imageView1;
        imageView1 = new ImageView(context);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView1.setImageResource(GalImages[position]);
        (container).addView(imageView1, 0);
        return imageView1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
