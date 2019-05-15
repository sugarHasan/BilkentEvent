package com.example.bilkentevent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This is a modified version of AdapterEvent. It displays the date instead of the time.
 * @author İbrahim Karakoç & Hasan Yıldırım
 * @version 15/05/19
 */
public class AdapterAllEvents extends AdapterEvent {

    Context context;
    public AdapterAllEvents(Context context , int resourceId , List<Event> items){
        super(context,resourceId,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Get the data item for this position
        Event e = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_all_events, parent, false);
        }

        // Change the views.
        TextView topic = (TextView) convertView.findViewById(R.id.topic);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        topic.setText(e.getTopic());
        date.setText(e.getDayOfEvent().toString());
        return convertView;
    }
}

