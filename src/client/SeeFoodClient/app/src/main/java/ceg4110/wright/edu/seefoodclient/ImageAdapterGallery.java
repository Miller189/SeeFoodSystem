package ceg4110.wright.edu.seefoodclient;


import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.GridView;
import android.view.LayoutInflater;
import android.widget.TextView;



import android.view.View;


import android.widget.ImageView;


public class ImageAdapterGallery extends BaseAdapter {
    private Context mContext;

    Integer[] idImage ;
    String[] score ;
    String[] isFood ;


    public ImageAdapterGallery(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 8;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Bitmap[] mThumbIds = obj.getMThumbIds();


        View grid = null;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.gallery_items, null);
            TextView textView = (TextView) grid.findViewById(R.id.score);
            TextView textView2 = (TextView) grid.findViewById(R.id.isFodd);
            imageView = (ImageView)grid.findViewById(R.id.thumb);
            System.out.println("IN GETVIEW");
            //GridMerge adapter = new GridMerge(mContext, score, mThumbIds);
            textView.setText(score[position]);
            textView2.setText(isFood[position]);
            imageView.setImageResource(idImage[position]);

            //imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            grid = (View) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        return grid;
    }


//
//    // references to our images
//
//     int[] mThumbIds = {
//            R.drawable.ic_launcher,
//             R.drawable.ic_launcher,
//             R.drawable.ic_launcher,
//             R.drawable.ic_launcher,
//             R.drawable.ic_launcher,
//             R.drawable.ic_launcher,
//             R.drawable.ic_launcher,
//             R.drawable.ic_launcher
//    };
//     String[] score = {
//            "a score",
//             "a score",
//             "a score",
//             "a score",
//             "a score",
//             "a score",
//             "a score",
//             "a score"
//    };
//
//    String[] isFood = {
//            "Is Food",
//            "No Food",
//            "Is Food",
//            "No Food",
//            "Is Food",
//            "No Food",
//            "Is Food",
//            "No Food"
//    };
}