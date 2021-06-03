package com.example.retrofit2stub;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageListAdapter extends BaseAdapter {
    Context context;
    Hit[] hits;
    public ImageListAdapter(Context context, Hit[] hits){
        this.context = context;
        this.hits = hits;
    }

    @Override
    public int getCount() {
        return hits.length;
    }

    @Override
    public Object getItem(int i) {
        return hits[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.image_list_item, viewGroup,false);
        ImageView imageView = view.findViewById(R.id.imageView1);
        Picasso.get().load(hits[i].recipe.image).into(imageView);
        //imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image));
        TextView txt = view.findViewById(R.id.textLabel);
        txt.setText(hits[i].recipe.label);
        return view;
    }
}
