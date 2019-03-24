package com.example.bilkentevent;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {

    Context context;
    public arrayAdapter(Context context , int resourceId , List<Cards>items){
        super(context,resourceId,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Cards cardItem = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item , parent , false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(cardItem.getName());
        image.setImageResource(R.mipmap.ic_launcher);

        return convertView;
    }

}
