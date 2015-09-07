package com.sciencedefine.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by naveenagrawal on 07-Sep-15.
 */
public class ImageAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[] images;
    public ImageAdapter(Context context, int[] images) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return images[i];
    }

    @Override
    public long getItemId(int i) {
        return images[i];
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            convertView = this.layoutInflater.inflate(R.layout.list,
                    parent, false);
        }
        else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(images[i]);
        return convertView;
    }

}
